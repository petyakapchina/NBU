package systemA;


public class Plumber {
    public static void main(String argv[]) {
        String fileName = "resources/FlightData.dat";

        SourceFilter Filter1 = new SourceFilter(fileName);
        SinkFilter Filter2 = new SinkFilter();

        Filter2.Connect(Filter1);

        Filter1.start();
        Filter2.start();
    }
}