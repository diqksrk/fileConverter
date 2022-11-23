package com.example.demo.model;

import com.example.demo.exception.NotFileException;
import com.example.demo.exception.NotHaveFileException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class FileCreator {
    private static Logger logger = LoggerFactory.getLogger(FileCreator.class);
    public static String sourceName = "/log";
    private static String defaultFileName = "/none";
    private static String NotFileExceptionMessage = "Not File.";
    private static String NotHaveFileExceptionMessage = "Log File is Not Exist.";
    private static BufferedReader bufferedReader;
    private LineReader lineReader;

    private int count = 0;
    private int outputBufferTargetNumber = 1;



    private int stepAfterPartitionCount = 0;


    Map<String, Boolean> jobMap = new HashMap<>();
    Map<String, Boolean> partitionHashMap = new HashMap<>();
    Map<String, BufferedOutputStream> bufferdHashMap = new HashMap<>();

    public FileCreator(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public void createLogFiles(String path) throws IOException {
        File file = new File(path + sourceName);

        logger.info("readFile : " + path + sourceName);

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            validCheckFile(file);
            createLogPerFile(path);
            bufferedReader.close();
        } catch (Exception e) {
            logger.error("RunTime Exception: " + e, e);
            return;
        } finally {
            bufferedReader.close();
        }
    }

    private void createLogPerFile(String path) throws IOException {
        String stepName; String threadId;
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            threadId = lineReader.getThreadId(line).trim();
            stepName = lineReader.getStepName(line).trim();


            BufferedOutputStream bufferedOutputStream = getTempTargetPath(threadId, stepName, path);
//
//            BufferedOutputStream bufferedOutputStream = getBufferedOutputStream(getTargetPath(stepId, threadId, path));
            FileWriter.writeLine(line, bufferedOutputStream);
        }

        logger.info("count : " + count);
        bufferFlush();
    }

    private BufferedOutputStream getTempTargetPath(String threadId, String stepName, String path) throws FileNotFoundException {
        String threadIdAndStepName;
        BufferedOutputStream bufferedOutputStream;

        if (StringUtils.isBlank(stepName) && StringUtils.isBlank(threadId)) {



            threadIdAndStepName = lineReader.getPrevTargetPath();




            if (bufferdHashMap.get(threadIdAndStepName) == null) {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + String.valueOf(outputBufferTargetNumber++), true));
                bufferdHashMap.put(threadIdAndStepName, bufferedOutputStream);
            } else {
                bufferedOutputStream = bufferdHashMap.get(threadIdAndStepName);
            }

        } else if (!lineReader.isAfterStepZone()) {

            threadIdAndStepName = threadId;

            if (bufferdHashMap.get(threadIdAndStepName) == null) {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + String.valueOf(outputBufferTargetNumber++), true));
                bufferdHashMap.put(threadIdAndStepName, bufferedOutputStream);
            } else {
                bufferedOutputStream = bufferdHashMap.get(threadIdAndStepName);
            }


        } else if (!lineReader.isAfterPartitionZone()) {

            threadIdAndStepName = threadId + " " + stepName;

            if (bufferdHashMap.get(threadIdAndStepName) == null) {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + String.valueOf(outputBufferTargetNumber++), true));
                bufferdHashMap.put(threadIdAndStepName, bufferedOutputStream);
            } else {
                bufferedOutputStream = bufferdHashMap.get(threadIdAndStepName);
            }

        } else if (lineReader.isAfterPartitionZone() && !StringUtils.isBlank(stepName) && stepName.indexOf("partition") == -1) {

            threadIdAndStepName = threadId + " " + stepName;

            if (partitionHashMap.get(threadIdAndStepName) == null) {


                partitionHashMap.put(threadIdAndStepName, true);
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + String.valueOf(outputBufferTargetNumber++), true));

                lineReader.setReadAndThreadId(String.valueOf(outputBufferTargetNumber));

                bufferdHashMap.put(threadIdAndStepName + outputBufferTargetNumber, bufferedOutputStream);


            } else {
                bufferedOutputStream = bufferdHashMap.get(threadIdAndStepName);
            }



        } else if (lineReader.isAfterPartitionZone() && !StringUtils.isBlank(stepName) && stepName.indexOf("partition") != -1) {

            threadIdAndStepName = threadId + " " + stepName;

            if (bufferdHashMap.get(threadIdAndStepName) == null) {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + String.valueOf(outputBufferTargetNumber++), true));
                bufferdHashMap.put(threadIdAndStepName, bufferedOutputStream);
            } else {
                bufferedOutputStream = bufferdHashMap.get(threadIdAndStepName);
            }
        }



         else {

            threadIdAndStepName = threadId + " " + stepName;

            if (jobMap.get(threadIdAndStepName) == null) {
                jobMap.put(threadIdAndStepName, true);
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + String.valueOf(outputBufferTargetNumber++), true));

                lineReader.setReadAndThreadId(String.valueOf(outputBufferTargetNumber));

                bufferdHashMap.put(threadIdAndStepName + outputBufferTargetNumber, bufferedOutputStream);
            } else {
                bufferedOutputStream = bufferdHashMap.get(threadIdAndStepName);
            }


        }



        return bufferedOutputStream;
    }







//    private BufferedOutputStream getBufferedOutputStream(String targetPath) throws FileNotFoundException {
//        BufferedOutputStream bufferedOutputStream;
//
//        if (bufferdHashMap.get(targetPath) == null) {
//            logger.info("CreateFile = " + targetPath);
//            count++;
//            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetPath, true));
//            bufferdHashMap.put(targetPath, bufferedOutputStream);
//        } else {
//            bufferedOutputStream = bufferdHashMap.get(targetPath);
//        }
//
//        return bufferedOutputStream;
//    }

    private void bufferFlush() throws IOException {
        for (BufferedOutputStream bufferedOutputStream : bufferdHashMap.values()) {
            bufferedOutputStream.close();
        }
    }

    private String getTargetPath(String stepId, String threadId, String path) {
        String targetPath = path + defaultFileName;

        if (!StringUtils.isBlank(threadId)) {
            targetPath = lineReader.getSequenceNumber() == null ? path + threadId : path + lineReader.getSequenceNumber() + " " + threadId;
        }

        if (StringUtils.isBlank(stepId) && StringUtils.isBlank(threadId)) {
            targetPath = path + lineReader.getPrevTargetPath();
        }

        return targetPath;
    }

    private void validCheckFile(File file) {
        if (!file.isFile()) {
            throw new NotFileException(NotFileExceptionMessage);
        }

        if (!existSourceFile()) {
            throw new NotHaveFileException(NotHaveFileExceptionMessage);
        }
    }

    private boolean existSourceFile() {
        if (bufferedReader == null) return false;

        return true;
    }
}
