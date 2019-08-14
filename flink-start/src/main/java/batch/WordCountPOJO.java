package batch;

import lombok.Data;
import lombok.ToString;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.util.Collector;
import window.CountWindowTest2;

/**
 * @author: Mr.Yang
 * @create: 2019-07-04
 */
public class WordCountPOJO {
	public static void main(String[] args) throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.getConfig().disableForceKryo();
		env.registerType(Word.class);

		DataSource<String> text = env.fromElements(CountWindowTest2.WordCountData.WORDS);
		text.flatMap(new Tokenizer())
				.groupBy(Word::getWord)
				.reduce(new ReduceFunction<Word>() {
					@Override
					public Word reduce(Word value1, Word value2) throws Exception {
						return new Word(value1.getWord(), value1.getFrequency() + value2.getFrequency());
					}
				}).print();
	}

	/**
	 * 转换为word{@link Word}对象
	 */
	private static class Tokenizer implements FlatMapFunction<String, Word> {

		@Override
		public void flatMap(String value, Collector<Word> out) throws Exception {
			String regex = "\\W+";
			String[] words = value.toLowerCase().split(regex);

			for (String word : words) {
				if (word.length() > 0) {
					out.collect(new Word(word, 1));
				}
			}
		}
	}

	/**
	 * 一个单词和词频的POJO类
	 */
	@Data
	@ToString
	public static class Word {
		private String word;
		private int frequency;

		public Word() {
		}

		public Word(String word, int frequency) {
			this.word = word;
			this.frequency = frequency;
		}
	}
}
