package systemA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static systemA.Constants.ALTITUDE_INDEX;
import static systemA.Constants.HEADER_LENGTH;
import static systemA.Constants.TEMPERATURE_INDEX;
import static systemA.Constants.TIME_INDEX;

public class SinkFilter extends FilterFramework {

    public void run() {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            Calendar TimeStamp = Calendar.getInstance();
            SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:DD:HH:mm:ss:SSS");

            int bytes_read = 0;

            long measurement;

            double altitude = 0;
            double temperature = 0;

            fileWriter = new FileWriter("src/systemA/OutputA.dat");
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Time:          Temperature (C):        Altitude (m):");
            bufferedWriter.newLine();
            bufferedWriter.write("----------------------------------------------------");
            bufferedWriter.newLine();

            while (true) {
                try {

                    byte[] header = new byte[HEADER_LENGTH];
                    bytes_read += ReadFilterInputPort(header);
                    int index = super.getIndex(header);
                    measurement = getMeasurement(header);

                    if (index == TIME_INDEX) {
                        TimeStamp.setTimeInMillis(measurement);
                        altitude = 0;
                        temperature = 0;
                    } else if (index == ALTITUDE_INDEX) {
                        altitude = Double.longBitsToDouble(measurement) / 3.28;

                    } else if (index == TEMPERATURE_INDEX) {
                        temperature = Double.longBitsToDouble((measurement) - 32) * 5 / 9;
                    }

                    if (temperature != 0 && altitude != 0) {
                        bufferedWriter.write(TimeStampFormat.format(TimeStamp.getTime()) + "   " +
                                formatDecimal(temperature) + "     " + formatDecimal(altitude));
                        bufferedWriter.newLine();
                    }

                } catch (
                        EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::System A Exiting; bytes read: " + bytes_read);
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

    private String formatDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00000");
        decimalFormat.setMaximumFractionDigits(5);
        decimalFormat.setMinimumFractionDigits(5);
        return decimalFormat.format(value);
    }

}

