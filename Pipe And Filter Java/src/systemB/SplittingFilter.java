package systemB;

import java.util.ArrayDeque;
import java.util.Deque;

import static systemB.Constants.HEADER_LENGTH;
import static systemB.Constants.PRESSURE_INDEX;
import static systemB.Constants.PRESSURE_THRESHOLD_HIGH;
import static systemB.Constants.PRESSURE_THRESHOLD_LOW;
import static systemB.Constants.TIME_INDEX;

public class SplittingFilter extends FilterSplitterFramework {

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

                int index = getIndex(header);

                int size = frame.size();
                // next frame has arrived - manage previous one
                if (index == TIME_INDEX && size > 0) {
                    writeToPipe(frame, badData);
                    bytes_written += HEADER_LENGTH * size;
                    if (badData) {
                        bytes_written_bad += HEADER_LENGTH * size;
                    }
                    badData = false;
                }

                if (index == PRESSURE_INDEX) {
                    double pressure = super.getPressure(header);
                    if (pressure < PRESSURE_THRESHOLD_LOW || pressure > PRESSURE_THRESHOLD_HIGH) { // bad case -> mark for bad pipe
                        markForExtrapolation(header);
                        badData = true;
                    }
                }
                frame.offer(header);
            } catch (FilterFramework.EndOfStreamException e) {
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
}