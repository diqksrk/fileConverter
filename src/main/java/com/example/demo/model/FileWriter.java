package com.example.demo.model;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class FileWriter {
    private static String lineSeparator = System.getProperty("line.separator");

    public static void writeLine(String line, BufferedOutputStream bufferedOutputStream) throws IOException {
        byte[] data;

        data = line.getBytes();
        bufferedOutputStream.write(data);
        bufferedOutputStream.write(lineSeparator.getBytes());
    }
}
