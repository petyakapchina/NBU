package systemA;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static systemA.Constants.HEADER_LENGTH;
import static systemA.Constants.INDEX_LENGTH;
import static systemA.Constants.MEASUREMENT_LENGTH;

public abstract class FilterFramework extends Thread {

    private PipedInputStream InputReadPort = new PipedInputStream();
    private PipedOutputStream OutputWritePort = new PipedOutputStream();

    private FilterFramework InputFilter;

    class EndOfStreamException extends Exception {
        EndOfStreamException(String s) {
            super(s);
        }
    }

    void Connect(FilterFramework Filter) {
        try {
            InputReadPort.connect(Filter.OutputWritePort);
            InputFilter = Filter;
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);
        }
    }

    int ReadFilterInputPort(byte b[]) throws EndOfStreamException {
        try {
            while (InputReadPort.available() < b.length) {
                if (EndOfInputStream()) {
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
            return InputReadPort.read(b);
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
            return 0;
        }
    }

    void WriteFilterOutputPort(byte[] datum) {
        try {
            OutputWritePort.write(datum);
            OutputWritePort.flush();
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + Error);
        }
    }

    private boolean EndOfInputStream() {
        if (InputFilter.isAlive()) {
            return false;
        }
        return true;
    }

    void ClosePorts() {
        try {
            InputReadPort.close();
            OutputWritePort.close();
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);
        }
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

}