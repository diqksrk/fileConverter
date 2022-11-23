package com.example.demo.model;

import com.example.demo.utils.FileSortComparator;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

public class FileMerger {
    private static Logger logger = LoggerFactory.getLogger(FileMerger.class);
    public static final String defaultTargetFileName = "/sequenceLog";
    private static BufferedReader bufferedReader;
    private static BufferedOutputStream bufferedOutputStream;
    private int count = 0;

    public void merge(String path) throws IOException {
        String targetPath = path + defaultTargetFileName;

        File[] files = new File(path).listFiles();
        Arrays.sort(files, new FileSortComparator());

        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetPath, true));

            logger.info("Start Merge File");
            for (File file : files) {
                if (isMergeExceptFile(file)) continue;

                logger.info("Merge File : " + file.getName());
                createSequenceLogFile(file);
                count++;
            }
        } catch (Exception e) {
            logger.error("RunTime Exception: " + e, e);
            return;
        } finally {
            logger.info("Merge File Count : " + count);
            logger.info("Merged File : " + targetPath);
            bufferedOutputStream.close();
        }
    }

    private boolean isMergeExceptFile(File file) {
        if (file.getName().equals("log")) {
            logger.info("File Merge Except File : " + file.getName());
            return true;
        }
        if (file.getName().equals("build.xml")) {
            logger.info("File Merge Except File : " + file.getName());
            return true;
        }
        if (file.getName().equals("changelog.xml")) {
            logger.info("File Merge Except File : " + file.getName());
            return true;
        }
        if (file.getName().equals(ThreadContext.get("fileCreatePosition"))) {
            logger.info("File Merge Except File : " + file.getName());
            return true;
        }
        if (file.isDirectory()) {
            logger.info("Directory File : " + file.getName());
            return true;
        }

        return false;
    }

    private void createSequenceLogFile(File file) throws IOException {
        String line;
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        while (  (line = bufferedReader.readLine()) != null  ) {
            FileWriter.writeLine(line, bufferedOutputStream);
        }

        bufferedReader.close();
    }
}
