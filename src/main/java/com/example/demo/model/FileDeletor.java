package com.example.demo.model;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileDeletor {
    private static Logger logger = LoggerFactory.getLogger(FileDeletor.class);

    public void delete(String path) {
        File[] files = new File(path).listFiles();

        try {
            for (File file : files) {
                if (isDeleteExceptFile(file)) continue;

                file.delete();
            }
        } catch (Exception e) {
            logger.error("RunTime Exception: " + e, e);
            return;
        }
    }

    private boolean isDeleteExceptFile(File file) {
        if (file.getName().equals("sequenceLog")) {
            logger.info("Except File Delete Except File : " + file.getName());
            return true;
        }
        if (file.getName().equals("build.xml")) {
            logger.info("Except File Delete Except File : " + file.getName());
            return true;
        }
        if (file.getName().equals("changelog.xml")) {
            logger.info("Except File Delete Except File : " + file.getName());
            return true;
        }
        if (file.getName().equals(ThreadContext.get("fileCreatePosition"))) {
            logger.info("Except File Merge Except File : " + file.getName());
            return true;
        }
        if (file.isDirectory()) {
            logger.info("Except Directory File : " + file.getName());
            return true;
        }

        return false;
    }
}
