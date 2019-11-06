package JDBC.util;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.template.TemplateAnalyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Mr.Yang
 * @create: 2019-08-22
 */
public class TemplateAnalyzerUtil {
	static {
		try {
			DicInitializer.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static Map<String, TemplateAnalyzer> templateMap = new HashMap<>();

	/**
	 * 创建一个模板分析器并返回
	 * @param tplName
	 * @param template
	 * @return
	 */
	public static TemplateAnalyzer getTplAnalyzer(String tplName, String template) {
		if (!templateMap.containsKey(tplName)) {
			TemplateAnalyzer tplAnalyzer = new TemplateAnalyzer(tplName, template);
			templateMap.put(tplName, tplAnalyzer);
		}

		return templateMap.get(tplName);
	}

	/**
	 * 获取一个已存在的模板分析器，
	 * 如果不存在会抛出异常
	 * @param tplName
	 * @return
	 */
	public static TemplateAnalyzer getTplAnalyzer(String tplName) {
		if (!templateMap.containsKey(tplName)) {
			throw new RuntimeException("Template not exists");
		}

		return templateMap.get(tplName);
	}
}
