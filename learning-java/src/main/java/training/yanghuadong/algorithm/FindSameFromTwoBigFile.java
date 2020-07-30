package training.yanghuadong.algorithm;

import com.google.common.hash.Hashing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在内存有限的条件下，从两个超大文件中找出相同的行
 * 思路:
 * 1. 单独遍历每一个文件，对每一行做hash，
 * 2. 根据hash值的不同，将这行数据写到不同的小文件,文件的选取为：hash(line)%1000
 * 3. 相同的行一定hash值相同，一定会被写入到序号相同的文件中。
 *
 * @author BirdSnail
 * @date 2020/7/30
 */
public class FindSameFromTwoBigFile {

	public static void main(String[] args) {

	}

	public static void split(Path file) throws IOException {
		List<String> allLines = Files.readAllLines(file);
		Map<String, List<String>> listMap = allLines.stream()
				.collect(Collectors.groupingBy(s -> s.hashCode() % 10 + ""));

	}

//	private static String hash(String line) {
//		return Hashing.sha256()
//				.hashString(line, StandardCharsets.UTF_8)
//				.toString();
//	}

}
