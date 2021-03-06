package window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import window.util.SplitWordsFunction;

/**
 * 流式的窗口单词统计，每来2个单词就会输出最近5个单词的出现次数
 * @Author yanghuadong
 * @DATE 2019/6/18 21:39
 */
public class CountWindowTest2 {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new RuntimeException("Usage:java -jar WordCount.jar --input <path> --output <path> --window <n> --slide <n>");
        }

        final ParameterTool params = ParameterTool.fromArgs(args);

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 获取输入数据
        DataStream<String> text;
        if (params.has("input")) {
            text = env.fromElements(WordCountData.WORDS);
            text = env.socketTextStream("bear", 10086);
        } else {
            text = env.socketTextStream("bear", 10086);
        }

        final long windowSize = params.getLong("window",5);
        final long slideSize = params.getLong("slide",2);

        text.flatMap(new SplitWordsFunction())
                .keyBy(0)
                // keyby分区过后是每个key里面元素的数量到达2个就触发运算
                //.countWindow(windowSize, slideSize)
                .sum(1)
                .print();

        env.execute("WindowWordCount");
    }

    // *****************************************************************************************************************
    //                            UTIL CLASS
    // *****************************************************************************************************************

    private static class Tuple2MapFunction implements MapFunction<String, Tuple2<String, Long>> {

        @Override
        public Tuple2<String, Long> map(String word) throws Exception {
            return Tuple2.of(word, 1L);
        }
    }

    // *****************************************************************************************************************
    // WORDS
    // *****************************************************************************************************************
    public static class WordCountData {

        public static final String[] WORDS = new String[]{
                "To be, or not to be,--that is the question:--",
                "Whether 'tis nobler in the mind to suffer",
                "The slings and arrows of outrageous fortune",
                "Or to take arms against a sea of troubles,",
                "And by opposing end them?--To die,--to sleep,--",
                "No more; and by a sleep to say we end",
                "The heartache, and the thousand natural shocks",
                "That flesh is heir to,--'tis a consummation",
                "Devoutly to be wish'd. To die,--to sleep;--",
                "To sleep! perchance to dream:--ay, there's the rub;",
                "For in that sleep of death what dreams may come,",
                "When we have shuffled off this mortal coil,",
                "Must give us pause: there's the respect",
                "That makes calamity of so long life;",
                "For who would bear the whips and scorns of time,",
                "The oppressor's wrong, the proud man's contumely,",
                "The pangs of despis'd love, the law's delay,",
                "The insolence of office, and the spurns",
                "That patient merit of the unworthy takes,",
                "When he himself might his quietus make",
                "With a bare bodkin? who would these fardels bear,",
                "To grunt and sweat under a weary life,",
                "But that the dread of something after death,--",
                "The undiscover'd country, from whose bourn",
                "No traveller returns,--puzzles the will,",
                "And makes us rather bear those ills we have",
                "Than fly to others that we know not of?",
                "Thus conscience does make cowards of us all;",
                "And thus the native hue of resolution",
                "Is sicklied o'er with the pale cast of thought;",
                "And enterprises of great pith and moment,",
                "With this regard, their currents turn awry,",
                "And lose the name of action.--Soft you now!",
                "The fair Ophelia!--Nymph, in thy orisons",
                "Be all my sins remember'd."
        };
    }

}
