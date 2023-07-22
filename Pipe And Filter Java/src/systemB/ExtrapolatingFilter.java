package systemB;

import java.util.ArrayDeque;
import java.util.Deque;

import static systemB.Constants.HEADER_LENGTH;
import static systemB.Constants.PRESSURE_EXTRAPOLATED_INDEX;
import static systemB.Constants.PRESSURE_INDEX;
import static systemB.Constants.TIME_INDEX;


public class ExtrapolatingFilter extends FilterSplitterFramework {

    public void run() {

        double previous_pressure = 0;
        double next_pressure = 0;
        int bytes_read = 0;
        int bytes_written = 0;
        Deque<byte[]> frame = new ArrayDeque<>();
        Deque<byte[]> extrapolatedFrames = new ArrayDeque<>();
        boolean extrapolated = false;


        while (true) {

            try {
                byte[] header = new byte[HEADER_LENGTH];
                ReadFilterInputPort(header);
                bytes_read += HEADER_LENGTH;

                int header_index = getIndex(header);
                int size = frame.size();
                if (header_index == TIME_INDEX && size > 0) { // new frame
                    if (!extrapolated) {
                        if (extrapolatedFrames.size() > 0) {
                            int e_size = extrapolatedFrames.size();
                            double extrapolated_pressure = calculateExtrapolatedValue(previous_pressure, next_pressure);
                            writeExtrapolatedToPipe(extrapolatedFrames, extrapolated_pressure);
                            bytes_written += e_size * HEADER_LENGTH;
                        }
                        writeToPipe(frame);
                        bytes_written += size * HEADER_LENGTH;
                    } else { // if in previous frame an extrapolated value was detected, put it here until a valid frame arrives
                        extrapolatedFrames.addAll(frame);
                        frame.clear();
                    }
                }

                if (header_index == PRESSURE_INDEX) {
                    double pressure = getPressure(header);
                    previous_pressure = next_pressure;
                    next_pressure = pressure;
                    extrapolated = false;
                }
                if (header_index == PRESSURE_EXTRAPOLATED_INDEX) {
                    extrapolated = true;
                }

                frame.offer(header);

            } catch (FilterFramework.EndOfStreamException e) {
                int size = frame.size();
                if (size > 0) {
                    if (extrapolated) {
                        extrapolatedFrames.addAll(frame);
                    } else {
                        writeToPipe(frame);
                        bytes_written += size * HEADER_LENGTH;
                    }
                }
                int e_size = extrapolatedFrames.size();
                if (e_size > 0) {
                    writeExtrapolatedToPipe(extrapolatedFrames, next_pressure);
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
