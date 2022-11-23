package com.example.demo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.example.demo.model.FileCreator.sourceName;
import static com.example.demo.model.FileMerger.defaultTargetFileName;

public class FileUpdator {
    private static Logger logger = LoggerFactory.getLogger(FileMerger.class);
    private static final String updateDefaultFileName = "sequenceLog";

    public void update(String path) {
        File file = new File(path + defaultTargetFileName);

        try {
            logger.info("final file name : log");

            if (file.getName().equals(updateDefaultFileName)) {
                fileRenameTo(file, updateDefaultFileName, path + sourceName);
            }
        } catch (Exception e) {
            logger.error("RunTime Exception: " + e, e);
            return;
        }
    }

    private void fileRenameTo(File file, String sourFileName, String targetFileName) {
        if (file.getName().equals(sourFileName)) {
            file.renameTo(new File(targetFileName));
        }
    }
}
