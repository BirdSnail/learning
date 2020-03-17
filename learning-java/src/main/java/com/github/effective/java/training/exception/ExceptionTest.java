package com.github.effective.java.training.exception;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author BirdSnail
 * @date 2019/12/10
 */
public class ExceptionTest implements Closeable {
    @Override
    public void close() {
        throw new RuntimeException("from IOManip.close");
    }

    private void test() {
        throw new RuntimeException();
    }
    public static void main(String[] args) {
        try (ExceptionTest ec = new ExceptionTest()) {
            throw new RuntimeException("from try!");
        }
    }

    static void readList(String path) {
        try {
            List<String> stringList = Files.readAllLines(new File(path).toPath());
            for (String s : stringList) {
                System.out.println(Integer.parseInt(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cat(File file) {
        RandomAccessFile input = null;
        String line = null;

        try {
            input = new RandomAccessFile(file, "r");
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
