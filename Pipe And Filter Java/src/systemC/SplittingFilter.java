package systemC;

import java.util.ArrayDeque;
import java.util.Deque;

import static systemC.Constants.ATTITUDE_EXTRAPOLATED_INDEX;
import static systemC.Constants.ATTITUDE_INDEX;
import static systemC.Constants.HEADER_LENGTH;
import static systemC.Constants.PRESSURE_EXTRAPOLATED_INDEX;
import static systemC.Constants.PRESSURE_INDEX;
import static systemC.Constants.TIME_INDEX;

public class SplittingFilter extends FilterSplitterFramework {

    private final int low_threshold;
    private final int high_threshold;
    private final int index;

    public SplittingFilter(int low_threshold, int high_threshold, int index) {
        this.low_threshold = low_threshold;
        this.high_threshold = high_threshold;
        this.index = index;
    }

    public void run() {

        int bytes_read = 0;
        int bytes_written = 0;
        int bytes_written_bad = 0;

        System.out.print("\n" + this.getName() + "::Splitter Reading ");

        Deque<byte[]> frame = new ArrayDeque<>();
        boolean badData = false;

        while (true) {

            try {

                byte[] header = new byte[HEADER_LENGTH];
                bytes_read += ReadFilterInputPort(header);

                int id = getIndex(header);

                int size = frame.size();
                // next frame has arrived - manage previous one
                if (id == TIME_INDEX && size > 0) {
                    writeToPipe(frame, badData);
                    bytes_written += HEADER_LENGTH * size;
                    if (badData) {
                        bytes_written_bad += HEADER_LENGTH * size;
                    }
                    badData = false;
                }

                if (id == index) {
                    double measurement = Double.longBitsToDouble(super.getMeasurement(header));
                    if (measurement < low_threshold || measurement > high_threshold) { // bad case -> mark for bad pipe
                        byte flag = getFlag(id);
                        markForExtrapolation(header, flag);
                        badData = true;
                    }
                }
                frame.offer(header);
            } catch (EndOfStreamException e) {
                int size = frame.size();
                writeToPipe(frame, badData);
                bytes_written += HEADER_LENGTH * size;
                if (badData) {
                    bytes_written_bad += HEADER_LENGTH * size;
                }

                ClosePorts();
                System.out.print("\n" + this.getName() + "::Splitter Exiting; bytes read: " + bytes_read + " bytes written: " + bytes_written + "; bytes written to secoond pipe: " + bytes_written_bad);
                break;
            }
        }
    }


    private byte getFlag(int id) {
        switch (id) {
            case PRESSURE_INDEX:
                return (byte) PRESSURE_EXTRAPOLATED_INDEX;
            case ATTITUDE_INDEX:
                return (byte) ATTITUDE_EXTRAPOLATED_INDEX;
            default:
                return (byte) -1;
        }
    }
}
