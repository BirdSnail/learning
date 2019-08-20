package project;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.template.TemplateAnalyzer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;

/**
 * 模拟数据源
 *
 * 数据格式：
 *  厂商  地区  等级  时间  ip  域名  流量
 * @author: Mr.Yang
 * @create: 2019-08-19
 */
public class Simulator {

	public static final String TOPIC = "test";
//	private static Random random = new Random();

	public static void main(String[] args) throws Exception {
		//加载词典(只需执行一次即可)
		DicInitializer.init();
		//编辑模版
		String tplName = "abc.tpl";
		String tpl = "$Dic{name}\tCN\t$Dic{level}\t$Dic{time}\t$Dic{ip}\t$Dic{domain}\t$Dic{traffic}";
		//创建模版分析器（一个模版new一个TemplateAnalyzer对象即可）
		TemplateAnalyzer testTplAnalyzer = new TemplateAnalyzer(tplName, tpl);
		//分析模版生成模拟数据
		// two	CN	E	1566201098295	183.225.139.16	v5.go2yd.com	9938
//		String abc = testTplAnalyzer.analyse();
//		//打印分析结果
//		System.out.println(abc);

		//=====================================kafka======================================================
		Properties props = new Properties();
		props.put("bootstrap.servers", "bear:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer = new KafkaProducer<>(props);

		while (true) {
			String record = testTplAnalyzer.analyse();
			System.out.println(record);

			producer.send(new ProducerRecord<String, String>(TOPIC, record));
			Thread.sleep(2000L);
		}
	}

}
