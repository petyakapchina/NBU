package systemC;

import java.util.Queue;

public class MergeFilter extends FilterFramework {


    private final Queue<byte[]> mergedFrames;

    public MergeFilter(Queue<byte[]> mergedFrames) {
        this.mergedFrames = mergedFrames;
    }


    public void run() {
        int bytes_read = 0;
        int bytes_written = 0;

        try {
            Thread.sleep(300);
            while (!mergedFrames.isEmpty()) {
                byte[] arr = mergedFrames.poll();
                bytes_read += arr.length;
                WriteFilterOutputPort(arr);
                bytes_written += arr.length;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ClosePorts();
            System.out.println("\n" + this.getName() + "::Read file complete, bytes read::" + bytes_read + " bytes written: " + bytes_written);

        } catch (
                Exception closeErr) {
            System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeErr);
        }

    }
}
