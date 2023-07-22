package systemC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static systemC.Constants.ATTITUDE_EXTRAPOLATED_INDEX;
import static systemC.Constants.HEADER_LENGTH;
import static systemC.Constants.PRESSURE_EXTRAPOLATED_INDEX;
import static systemC.Constants.TIME_INDEX;

public class AbnormalSinkFiler extends FilterSplitterFramework {

    private final int index;

    public AbnormalSinkFiler(int index) {
        this.index = index;
    }

    public void run() {


        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            Calendar TimeStamp = Calendar.getInstance();
            SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:DD:HH:mm:ss:SSS");

            int headerLength = HEADER_LENGTH;
            int bytesRead = 0;

            long measurement;
            double pressure = 0;

            fileWriter = new FileWriter("src/systemC/WildPoints_" + getFileSuffix(index));
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Time:                          " + getLabel(index));
            bufferedWriter.newLine();
            bufferedWriter.write("------------------------------------------------");
            bufferedWriter.newLine();

            while (true) {
                try {

                    byte[] header = new byte[headerLength];
                    bytesRead += ReadFilterBadInputPort(header);
                    int id = super.getIndex(header);

                    measurement = super.getMeasurement(header);

                    if (id == TIME_INDEX) {
                        TimeStamp.setTimeInMillis(measurement);
                    } else if (id == index) {
                        pressure = Double.longBitsToDouble(measurement);

                    }
                    if (pressure != 0) {
                        bufferedWriter.write(TimeStampFormat.format(TimeStamp.getTime()) + "     " + formatDecimal(pressure));
                        bufferedWriter.newLine();
                        pressure = 0;
                    }
                } catch (
                        EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::System C Bad Sink Exiting; bytes read: " + bytesRead);
                    break;

                }

            }
        } catch (IOException e) {
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private String getLabel(int index) {
        switch (index) {
            case PRESSURE_EXTRAPOLATED_INDEX:
                return "Pressure(psi):";
            case ATTITUDE_EXTRAPOLATED_INDEX:
                return "Attitude(psi):";
            default:
                return "";
        }
    }

    private String getFileSuffix(int index) {
        switch (index) {
            case PRESSURE_EXTRAPOLATED_INDEX:
                return "pressure.dat";
            case ATTITUDE_EXTRAPOLATED_INDEX:
                return "attitude.dat";
            default:
                return "";
        }
    }
}



