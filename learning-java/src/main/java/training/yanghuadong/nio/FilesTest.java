package training.yanghuadong.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 使用 NIO 的{@link Files}遍历一个目录树进行文件的查找
 *
 * @author BirdSnail
 * @date 2019/12/3
 */
public class FilesTest {


    public static void main(String[] args) {
        String fileName = "README.md";
        findFilePath(fileName);
    }

    private static void findFilePath(String fileName) {
        Path rootPath = Paths.get("");
        String fileToFind = File.separator + fileName;

        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileString = file.toAbsolutePath().toString();
//                    System.out.println("pathString = " + fileString);

                    if(fileString.endsWith(fileToFind)){
                        System.out.println("file found at path: " + file.toAbsolutePath());
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
