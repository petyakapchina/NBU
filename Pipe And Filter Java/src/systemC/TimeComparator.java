package systemC;

import java.util.Arrays;
import java.util.Comparator;

import static systemC.Constants.HEADER_LENGTH;
import static systemC.Constants.INDEX_LENGTH;

public class TimeComparator implements Comparator<byte[]> {

    @Override
    public int compare(byte[] frame1, byte[] frame2) {
        long timeA = extractTime(frame1);
        long timeB = extractTime(frame2);
        return Math.toIntExact(timeA - timeB);
    }


    private static long extractTime(byte[] a) {
        byte[] extractedBytes = Arrays.copyOfRange(a, INDEX_LENGTH, HEADER_LENGTH);

        long combinedValue = 0;
        for (byte b : extractedBytes) {
            combinedValue = (combinedValue << 8) | (b & 0xFF);
        }
        return combinedValue;
    }
}
