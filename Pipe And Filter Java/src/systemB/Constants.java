package systemB;

public class Constants {

    private Constants() {
    }

    static final int HEADER_LENGTH = 12;

    static final int TIME_INDEX = 0;
    static final int ALTITUDE_INDEX = 2;
    static final int PRESSURE_INDEX = 3;
    static final int TEMPERATURE_INDEX = 4;
    static final int PRESSURE_EXTRAPOLATED_INDEX = 9;

    static final int INDEX_LENGTH = 4;
    static final int MEASUREMENT_LENGTH = 8;

    static final int PRESSURE_THRESHOLD_LOW = 50;
    static final int PRESSURE_THRESHOLD_HIGH = 80;


}
