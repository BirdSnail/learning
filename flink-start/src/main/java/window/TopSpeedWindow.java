package window;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.delta.DeltaFunction;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.evictors.DeltaEvictor;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.DeltaTrigger;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 获取一辆汽车每次行驶距离超过50时，最近十秒内的最高速度。
 * Trigger：两个point之间间隔超50就会触发窗口的计算。
 * Evictor：保留窗口10s内的元素
 * <p>
 * car:
 * id,speed,distance,time
 *
 * @author: Mr.Yang
 * @create: 2019-06-26
 */
public class TopSpeedWindow {

    public static void main(String[] args) throws Exception {
        // 参数检查
        final ParameterTool params = ParameterTool.fromArgs(args);
        params.has("input");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple4<Integer, Integer, Double, Long>> carData = env.addSource(CarSource.create(1));
        carData.assignTimestampsAndWatermarks(new CarTimestamp())
                .keyBy(0)
                .window(GlobalWindows.create())
                .evictor(TimeEvictor.of(Time.of(10L, TimeUnit.SECONDS)))
                .trigger(DeltaTrigger.of(50d,
                        new DeltaFunction<Tuple4<Integer, Integer, Double, Long>>() {
                            @Override
                            public double getDelta(Tuple4<Integer, Integer, Double, Long> oldDataPoint, Tuple4<Integer, Integer, Double, Long> newDataPoint) {
                                double delta = newDataPoint.f2 - oldDataPoint.f2;
                                System.out.println(newDataPoint.f0 + "号车距离上次距离：" + delta+"; base: " + oldDataPoint.f2);
                                return delta;
                            }
                        }, carData.getType().createSerializer(env.getConfig())))
                .maxBy(1)
                .print();

        env.execute();
    }

    // *************************************************************************
    // USER FUNCTIONS
    // *************************************************************************

    private static class CarSource implements SourceFunction<Tuple4<Integer, Integer, Double, Long>> {

        private static final long serialVersionUID = 1L;
        private Integer[] speeds;
        private Double[] distances;

        private Random rand = new Random();

        private volatile boolean isRunning = true;

        private CarSource(int numOfCars) {
            speeds = new Integer[numOfCars];
            distances = new Double[numOfCars];
            Arrays.fill(speeds, 50);
            Arrays.fill(distances, 0d);
        }

        public static CarSource create(int cars) {
            return new CarSource(cars);
        }

        @Override
        public void run(SourceContext<Tuple4<Integer, Integer, Double, Long>> ctx) throws Exception {

            while (isRunning) {
                Thread.sleep(100);
                for (int carId = 0; carId < speeds.length; carId++) {
                    if (rand.nextBoolean()) {
                        speeds[carId] = Math.min(100, speeds[carId] + 5);
                    } else {
                        speeds[carId] = Math.max(0, speeds[carId] - 5);
                    }
                    distances[carId] += speeds[carId] / 3.6d;
                    Tuple4<Integer, Integer, Double, Long> record = new Tuple4<>(carId,
                            speeds[carId], distances[carId], System.currentTimeMillis());

                    System.out.println(record);
                    ctx.collect(record);
                }
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    private static class CarTimestamp extends AscendingTimestampExtractor<Tuple4<Integer, Integer, Double, Long>> {
        private static final long serialVersionUID = 1L;

        @Override
        public long extractAscendingTimestamp(Tuple4<Integer, Integer, Double, Long> element) {
            return element.f3;
        }
    }
}
