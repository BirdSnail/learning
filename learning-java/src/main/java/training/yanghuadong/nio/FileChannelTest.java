package training.yanghuadong.nio;

import lombok.SneakyThrows;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author BirdSnail
 * @date 2019/12/5
 */
public class FileChannelTest {

    @SneakyThrows
    public static void main(String[] args) {
        RandomAccessFile file = new RandomAccessFile("learning-java/test.txt", "rw");
        FileChannel channel = file.getChannel();
        System.out.println(channel.size());
//        readFile(channel);
//        writeFile(channel);
//        writeFileWithHole(channel, 20);
    }

    @SneakyThrows
    private static void writeFile(FileChannel fileChannel) {
        ByteBuffer byteWrite = ByteBuffer.allocate(48);
        byteWrite.put("hello nio test".getBytes());

        byteWrite.flip();
        while (byteWrite.hasRemaining()) {
            fileChannel.write(byteWrite);
        }
    }

    @SneakyThrows
    private static void writeFileWithHole(FileChannel fileChannel, long position) {
        ByteBuffer byteWrite = ByteBuffer.allocate(48);
        byteWrite.put("hello nio test".getBytes());

        byteWrite.flip();
        while (byteWrite.hasRemaining()) {
            fileChannel.write(byteWrite, position);
        }
    }

    @SneakyThrows
    private static void readFile(FileChannel fileChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        int read = fileChannel.read(byteBuffer);
        System.out.println("read bytes counts is " + read);

        byteBuffer.flip();
        byte[] words = new byte[byteBuffer.limit()];
        byteBuffer.get(words);
        System.out.println(new String(words));

        while (byteBuffer.hasRemaining()) {
            System.out.println((char) (byteBuffer.get()));
        }
    }

}
