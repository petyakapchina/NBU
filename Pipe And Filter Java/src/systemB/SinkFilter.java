package systemB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static systemB.Constants.ALTITUDE_INDEX;
import static systemB.Constants.HEADER_LENGTH;
import static systemB.Constants.PRESSURE_EXTRAPOLATED_INDEX;
import static systemB.Constants.PRESSURE_INDEX;
import static systemB.Constants.TEMPERATURE_INDEX;
import static systemB.Constants.TIME_INDEX;

public class SinkFilter extends FilterSplitterFramework {

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
            String pressure = null;

            fileWriter = new FileWriter("src/systemB/OutputB.dat");
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Time:          Temperature (C):        Altitude (m):        Pressure (psi):");
            bufferedWriter.newLine();
            bufferedWriter.write("---------------------------------------------------------------------------");
            bufferedWriter.newLine();

            while (true) {
                try {

                    byte[] header = new byte[HEADER_LENGTH];
                    bytes_read += ReadFilterInputPort(header);
                    int index = super.getIndex(header);

                    measurement = super.getMeasurement(header);

                    if (index == TIME_INDEX) {
                        TimeStamp.setTimeInMillis(measurement);
                        altitude = 0;
                        temperature = 0;
                        pressure = null;
                    } else if (index == ALTITUDE_INDEX) {
                        altitude = Double.longBitsToDouble(measurement) / 3.28;
                    } else if (index == PRESSURE_INDEX) {
                        double pressure_n = Double.longBitsToDouble(measurement);
                        pressure = formatDecimal(pressure_n);
                    } else if (index == TEMPERATURE_INDEX) {
                        temperature = Double.longBitsToDouble((measurement) - 32) * 5 / 9;
                    } else if (index == PRESSURE_EXTRAPOLATED_INDEX) {
                        double pressure_n = Double.longBitsToDouble(measurement);
                        pressure = formatDecimal(pressure_n) + "*";
                    }

                    if (temperature != 0 && altitude != 0 && pressure != null) {
                        bufferedWriter.write(TimeStampFormat.format(TimeStamp.getTime()) + "   " +
                                formatDecimal(temperature) + "     " + formatDecimal(altitude) + "     " + pressure);
                        bufferedWriter.newLine();
                    }

                } catch (
                        EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::System B Regular Sink Exiting; bytes read: " + bytes_read);
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

