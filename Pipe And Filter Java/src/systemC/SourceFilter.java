package systemC;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import static systemC.Constants.HEADER_LENGTH;
import static systemC.Constants.TIME_INDEX;


public class SourceFilter extends FilterFramework {

    private final String filePath;
    private final Queue<byte[]> queue;

    public SourceFilter(String filePath, Queue<byte[]> queue) {
        this.filePath = filePath;
        this.queue = queue;
    }

    public void run() {

        int bytes_read = 0;
        int bytes_written = 0;
        DataInputStream in = null;

        Deque<byte[]> frame = new ArrayDeque<>();

        try {
            in = new DataInputStream(new FileInputStream(filePath));
            System.out.println("\n" + filePath + "::Source reading file...");

            while (true) {
                byte[] header = new byte[HEADER_LENGTH];
                in.readFully(header);
                bytes_read += HEADER_LENGTH;

                int index = getIndex(header);
                int size = frame.size();
                if (index == TIME_INDEX && size > 0) {
                    bytes_written += writeWholeFrameToQueue(frame);
                    frame.clear();
                }
                frame.offer(header);
            }

        } catch (IOException eoferr) {
            System.out.println("\n" + this.getName() + "::End of file reached...");
        }
        try {
            if (frame.size() > 0) {
                bytes_written += writeWholeFrameToQueue(frame);
            }
            in.close();
            ClosePorts();
            System.out.println("\n" + this.getName() + "::Read " + filePath + " complete, bytes read::" + bytes_read + " bytes written: " + bytes_written);

        } catch (Exception closeerr) {
            System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeerr);
        }
    }

    private int writeWholeFrameToQueue(Deque<byte[]> frame) {
        int bytes_written = 0;
        int size = frame.size();
        byte[] result = new byte[size * HEADER_LENGTH];
        int destPos = 0;
        for (byte[] bytes : frame) {
            System.arraycopy(bytes, 0, result, destPos, bytes.length);
            destPos += bytes.length;
        }
        bytes_written += result.length;
        queue.offer(result);
        return bytes_written;
    }


}
