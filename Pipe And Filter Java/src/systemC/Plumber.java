package systemC;


import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import static systemC.Constants.ATTITUDE_EXTRAPOLATED_INDEX;
import static systemC.Constants.ATTITUDE_INDEX;
import static systemC.Constants.ATTITUDE_THRESHOLD_HIGH;
import static systemC.Constants.ATTITUDE_THRESHOLD_LOW;
import static systemC.Constants.PRESSURE_EXTRAPOLATED_INDEX;
import static systemC.Constants.PRESSURE_INDEX;
import static systemC.Constants.PRESSURE_THRESHOLD_HIGH;
import static systemC.Constants.PRESSURE_THRESHOLD_LOW;

public class Plumber {


    public static void main(String argv[]) {
        TimeComparator comparator = new TimeComparator();

        PriorityBlockingQueue<byte[]> queue = new PriorityBlockingQueue(50, comparator);

        Set<String> files = Set.of("resources/SubSetA.dat", "resources/SubSetB.dat");

        ExecutorService threadPool = Executors.newFixedThreadPool(files.size());

        for (String file : files) {
            threadPool.submit(() -> new SourceFilter(file, queue).start());
        }

        threadPool.shutdown();

        MergeFilter merged = new MergeFilter(queue);
        SplittingFilter pressureFilter = new SplittingFilter(PRESSURE_THRESHOLD_LOW, PRESSURE_THRESHOLD_HIGH, PRESSURE_INDEX);
        SplittingFilter attitudeFilter = new SplittingFilter(ATTITUDE_THRESHOLD_LOW, ATTITUDE_THRESHOLD_HIGH, ATTITUDE_INDEX);

        ExtrapolatingFilter ext_Pressure = new ExtrapolatingFilter(PRESSURE_INDEX, PRESSURE_EXTRAPOLATED_INDEX);
        ExtrapolatingFilter ext_Attitude = new ExtrapolatingFilter(ATTITUDE_INDEX, ATTITUDE_EXTRAPOLATED_INDEX);

        SinkFilter sink = new SinkFilter();

        AbnormalSinkFiler sinkAbnormalPressure = new AbnormalSinkFiler(PRESSURE_EXTRAPOLATED_INDEX);
        AbnormalSinkFiler sinkAbnormalAttitude = new AbnormalSinkFiler(ATTITUDE_EXTRAPOLATED_INDEX);

        pressureFilter.Connect(merged);
        ext_Pressure.Connect(pressureFilter);
        attitudeFilter.Connect(ext_Pressure);
        ext_Attitude.Connect(attitudeFilter);
        sink.Connect(ext_Attitude);

        sinkAbnormalPressure.ConnectBad(pressureFilter);
        sinkAbnormalAttitude.ConnectBad(attitudeFilter);

        merged.start();
        pressureFilter.start();
        attitudeFilter.start();
        ext_Pressure.start();
        ext_Attitude.start();
        sink.start();
        sinkAbnormalPressure.start();
        sinkAbnormalAttitude.start();

    }


}