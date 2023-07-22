package systemB;

public class Plumber {
    public static void main(String argv[]) {


        String fileName = "resources/FlightData.dat";

        SourceFilter source = new SourceFilter(fileName);

        SplittingFilter splitter = new SplittingFilter();
        ExtrapolatingFilter extrasolar = new ExtrapolatingFilter();
        SinkFilter goodSink = new SinkFilter();
        AbnormalSinkFilter badSink = new AbnormalSinkFilter();


        badSink.ConnectBad(splitter);
        goodSink.Connect(extrasolar);
        extrasolar.Connect(splitter);
        splitter.Connect(source);

        source.start();

        splitter.start();
        extrasolar.start();

        goodSink.start();
        badSink.start();



    }

}