package systemC;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Deque;

import static systemC.Constants.HEADER_LENGTH;
import static systemC.Constants.INDEX_LENGTH;
import static systemC.Constants.MEASUREMENT_LENGTH;

public abstract class FilterSplitterFramework extends FilterFramework {

    private PipedInputStream BadInputReadPort = new PipedInputStream();
    private PipedOutputStream BadOutputWritePort = new PipedOutputStream();

    private FilterSplitterFramework InputFilter;

    void ConnectBad(FilterSplitterFramework Filter) {
        try {
            BadInputReadPort.connect(Filter.BadOutputWritePort);
            InputFilter = Filter;
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);
        }
    }


    void WriteFilterBadOutputPort(byte[] datum) {
        try {
            BadOutputWritePort.write(datum);
            BadOutputWritePort.flush();
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + Error);
        }
    }

    int ReadFilterBadInputPort(byte b[]) throws EndOfStreamException {
        try {
            while (BadInputReadPort.available() == 0) {
                if (EndOfSplitInputStream()) {
                    throw new EndOfStreamException("End of input stream reached");
                }
                sleep(250);
            }
        } catch (EndOfStreamException Error) {
            throw Error;
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Error in read port wait loop::" + Error);
        }
        try {
            return BadInputReadPort.read(b);
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
            return 0;
        }
    }

    void ClosePorts() {
        try {
            super.ClosePorts();
            BadOutputWritePort.close();
            BadInputReadPort.close();
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);
        }
    }

    protected boolean EndOfSplitInputStream() {
        if (InputFilter.isAlive()) {
            return false;
        }
        return true;
    }

    protected void setMeasurement(byte[] a, double measurement) {
        ByteBuffer buffer = ByteBuffer.allocate(MEASUREMENT_LENGTH);
        buffer.putDouble(measurement);

        System.arraycopy(buffer.array(), 0, a, INDEX_LENGTH, buffer.array().length);
    }

    protected int getIndex(byte[] b) {
        int index = 0;
        for (int i = 0; i < INDEX_LENGTH; i++) {
            index = (index << MEASUREMENT_LENGTH) | (b[i] & 0xFF);
        }
        return index;
    }

    protected long getMeasurement(byte[] b) {
        long m = 0;
        for (int i = INDEX_LENGTH; i < HEADER_LENGTH; i++) {
            m = (m << MEASUREMENT_LENGTH) | (b[i] & 0xFF);
        }
        return m;
    }


    protected byte[] markForExtrapolation(byte[] measurement, byte flag) {
        measurement[INDEX_LENGTH - 1] = flag;
        return measurement;
    }

    protected void writeToPipe(Deque<byte[]> frame) {
        while (!frame.isEmpty()) {
            WriteFilterOutputPort(frame.pollFirst());
        }
    }

    protected void writeToPipe(Deque<byte[]> frame, boolean addToBadPipe) {
        while (!frame.isEmpty()) {
            byte[] header = frame.pollFirst();
            WriteFilterOutputPort(header);
            if (addToBadPipe) {
                WriteFilterBadOutputPort(header);
            }
        }
    }

    protected void writeExtrapolatedToPipe(Deque<byte[]> frame, double measurement, int index) {
        while (!frame.isEmpty()) {
            byte[] header = frame.pollFirst();
            if (getIndex(header) == index) {
                setMeasurement(header, measurement);
            }
            WriteFilterOutputPort(header);
        }
    }

}
