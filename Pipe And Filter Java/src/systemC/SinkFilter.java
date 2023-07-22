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

public class SinkFilter extends FilterFramework {

    public void run() {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            Calendar TimeStamp = Calendar.getInstance();
            SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:DD:HH:mm:ss:SSS");

            int bytes_read = 0;

            long measurement;

            fileWriter = new FileWriter("src/systemC/OutputC.dat");
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Time:        Velocity(k/h):       Altitude (m):        Pressure (psi):       Temperature (C):        Attitude(PSI):");
            bufferedWriter.newLine();
            bufferedWriter.write("-------------------------------------------------------------------------------------------------------------------");
            bufferedWriter.newLine();

            String formatted_measurement = null;

            while (true) {
                try {

                    byte[] header = new byte[HEADER_LENGTH];
                    bytes_read += ReadFilterInputPort(header);
                    int index = super.getIndex(header);
                    boolean newFrame = false;

                    measurement = super.getMeasurement(header);

                    if (index == TIME_INDEX) {
                        if (formatted_measurement != null) {
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                        formatted_measurement = null;
                        TimeStamp.setTimeInMillis(measurement);
                        bufferedWriter.write(TimeStampFormat.format(TimeStamp.getTime()));
                        newFrame = true;
                    } else {
                        double measurement_n = Double.longBitsToDouble(measurement);
                        formatted_measurement = String.format("%.5f", measurement_n);
                    }

                    if (index == ATTITUDE_EXTRAPOLATED_INDEX || index == PRESSURE_EXTRAPOLATED_INDEX) {
                        formatted_measurement += "*";
                    }

                    if (!newFrame) {
                        bufferedWriter.write("   " + formatted_measurement);
                    }

                } catch (EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::System C Regular Sink Exiting; bytes read: " + bytes_read);
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