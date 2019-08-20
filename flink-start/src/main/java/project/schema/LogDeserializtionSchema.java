package project.schema;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.StringUtils;
import project.RequestLog;

import java.io.IOException;

import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * kafka中的二进制数据反序列化成对象
 * @author: Mr.Yang
 * @create: 2019-08-20
 */
public class LogDeserializtionSchema implements DeserializationSchema<RequestLog>{

	/**类型信息*/
	private final TypeInformation<RequestLog> typeInfo;

	public LogDeserializtionSchema(TypeInformation<RequestLog> typeInfo) {
		this.typeInfo = checkNotNull(typeInfo,"typeInfo");
	}

	@Override
	public RequestLog deserialize(byte[] message) throws IOException {
		String log = new String(message);
		if (log == null)
			return null;
		String[] split = log.split("\t");
		if (split.length != 7) {
			return null;
		}

		return new RequestLog(split[0], split[1], split[2], Long.parseLong(split[3]), split[4], split[5],
				Integer.parseInt(split[6]));
	}

	@Override
	public boolean isEndOfStream(RequestLog nextElement) {
		return false;
	}

	@Override
	public TypeInformation<RequestLog> getProducedType() {
		return typeInfo;
	}
}
