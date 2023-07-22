package systemB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static systemB.Constants.HEADER_LENGTH;
import static systemB.Constants.PRESSURE_EXTRAPOLATED_INDEX;
import static systemB.Constants.TIME_INDEX;

public class AbnormalSinkFilter extends FilterSplitterFramework {
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

            fileWriter = new FileWriter("src/systemB/WildPoints.dat");
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Time:                          Pressure (psi):");
            bufferedWriter.newLine();
            bufferedWriter.write("------------------------------------------------");
            bufferedWriter.newLine();

            while (true) {
                try {

                    byte[] header = new byte[headerLength];
                    bytesRead += ReadFilterBadInputPort(header);
                    int index = super.getIndex(header);

                    measurement = super.getMeasurement(header);

                    if (index == TIME_INDEX) {
                        TimeStamp.setTimeInMillis(measurement);
                    } else if (index == PRESSURE_EXTRAPOLATED_INDEX) {
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
                    System.out.print("\n" + this.getName() + "::System B Bad Sink Exiting; bytes read: " + bytesRead);
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
}



