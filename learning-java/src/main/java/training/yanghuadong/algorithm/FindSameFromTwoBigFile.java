package training.yanghuadong.algorithm;


import java.io.IOException;
import java.nio.file.*;
import java.util.*;
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

    static int count = 10;

    public static void main(String[] args) throws IOException {
        Path fileA = Paths.get("E:\\ideaProject\\learning\\learning-java", "test.txt");
        Path fileB = Paths.get("E:\\ideaProject\\learning\\learning-java", "b.txt");

        Set<String> filesA = split(fileA, count);
        Set<String> filesB = split(fileB, count);
        filesB.retainAll(filesA);

        for (String key : filesB) {
            Path aPath = getSplitPath(fileA, key);
            Path bPath = getSplitPath(fileB, key);
            List<String> result = getSameLineFromPair(aPath, bPath);
            if (!result.isEmpty()) {
                System.out.println("存在相同的行：");
                result.forEach(System.out::println);
            }
        }
    }

    /**
     * 根据每行的hash值将改行写到不同的子文件中
     *
     * @param file  原始文件
     * @param count 子文件个数
     * @return 产生的子文件序号集合
     * @throws IOException io异常
     */
    public static Set<String> split(Path file, int count) throws IOException {
        List<String> allLines = Files.readAllLines(file);
        Map<String, List<String>> lineMap = allLines.stream()
                .collect(Collectors.groupingBy(s -> negativeHash(s) % count + ""));
        for (String key : lineMap.keySet()) {
            Path splitPath = getSplitPath(file, key);
            Files.write(splitPath, lineMap.get(key), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        return lineMap.keySet();
    }

    public static Path getSplitPath(Path file, String key) {
        String base = file.getFileName().toString();
        String directory = file.getParent().getFileName().toString();
        return Paths.get(directory, base + key);
    }

    public static List<String> getSameLineFromPair(Path one, Path two) throws IOException {
        if (!Files.exists(one) || !Files.exists(two)) {
            return Collections.emptyList();
        }
        List<String> linesOne = Files.readAllLines(one);
        List<String> linesTwo = Files.readAllLines(two);
        linesOne.retainAll(linesTwo);
        return linesOne;
    }

    // 保证hash值一定为正数
    private static int negativeHash(String s) {
        return s.hashCode() & Integer.MAX_VALUE;
    }

}
