package systemC;

import java.util.ArrayDeque;
import java.util.Deque;

import static systemC.Constants.HEADER_LENGTH;
import static systemC.Constants.TIME_INDEX;


public class ExtrapolatingFilter extends FilterSplitterFramework {

    private final int index;
    private final int extrapolated_index;

    public ExtrapolatingFilter(int index, int extrapolated_index) {
        this.index = index;
        this.extrapolated_index = extrapolated_index;
    }

    public void run() {

        double previous = 0;
        double next = 0;
        int bytes_read = 0;
        int bytes_written = 0;
        Deque<byte[]> frame = new ArrayDeque<>();
        Deque<byte[]> extrapolatedFrames = new ArrayDeque<>();
        boolean isExtrapolated = false;


        while (true) {

            try {
                byte[] header = new byte[HEADER_LENGTH];
                ReadFilterInputPort(header);
                bytes_read += HEADER_LENGTH;

                int header_index = getIndex(header);
                int size = frame.size();
                if (header_index == TIME_INDEX && size > 0) { // new frame
                    if (!isExtrapolated) {
                        if (extrapolatedFrames.size() > 0) {
                            int e_size = extrapolatedFrames.size();
                            double extrapolated = calculateExtrapolatedValue(previous, next);
                            writeExtrapolatedToPipe(extrapolatedFrames, extrapolated, extrapolated_index);
                            bytes_written += e_size * HEADER_LENGTH;
                        }
                        writeToPipe(frame);
                        bytes_written += size * HEADER_LENGTH;
                    } else { // if in previous frame an isExtrapolated value was detected, put it here until a valid frame arrives
                        extrapolatedFrames.addAll(frame);
                        frame.clear();
                    }
                }

                if (header_index == index) {
                    double measurement = Double.longBitsToDouble(super.getMeasurement(header));
                    previous = next;
                    next = measurement;
                    isExtrapolated = false;
                }
                if (header_index == extrapolated_index) {
                    isExtrapolated = true;
                }

                frame.offer(header);

            } catch (EndOfStreamException e) {
                int size = frame.size();
                if (size > 0) {
                    if (isExtrapolated) {
                        extrapolatedFrames.addAll(frame);
                    } else {
                        writeToPipe(frame);
                        bytes_written += size * HEADER_LENGTH;
                    }
                }
                int e_size = extrapolatedFrames.size();
                if (e_size > 0) {
                    writeExtrapolatedToPipe(extrapolatedFrames, next, extrapolated_index);
                    bytes_written += e_size * HEADER_LENGTH;
                }
                ClosePorts();
                System.out.print("\n" + this.getName() + "::Extrapolate Exiting; bytes read: " + bytes_read + " bytes written: " + bytes_written);
                break;

            }
        }
    }

    private double calculateExtrapolatedValue(double previous_pressure, double next_pressure) {
        if (previous_pressure != 0 && next_pressure != 0) {
            return (previous_pressure + next_pressure) / 2;
        } else {
            if (previous_pressure == 0) {
                return next_pressure;
            } else if (next_pressure == 0) {
                return previous_pressure;
            }
            return 0;
        }
    }
}
