package project;

import lombok.Data;
import lombok.ToString;

/**
 * 一个pojo，对应一条请求日志
 *
 * @author: Mr.Yang
 * @create: 2019-08-20
 */
@Data
@ToString
public class RequestLog {

	/**
	 * 厂商名称
	 */
	private String name;

	/**
	 * 地区
	 */
	private String area;

	/**
	 * 等级
	 */
	private String level;

	/**
	 * 访问时间
	 */
	private long time;

	/**
	 * ip
	 */
	private String ip;
	/**
	 * 域名
	 */
	private String domain;

	/**
	 * 访问量
	 */
	private int traffic;

	public RequestLog() {

	}

	public RequestLog(String name, String area, String level, long timestamp, String ip, String domain, int traffic) {
		this.name = name;
		this.area = area;
		this.level = level;
		this.time = timestamp;
		this.ip = ip;
		this.domain = domain;
		this.traffic = traffic;
	}
}
