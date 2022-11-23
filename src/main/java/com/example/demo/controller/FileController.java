package com.example.demo.controller;

import com.example.demo.model.*;

import java.io.IOException;

public class FileController {

    public void generate(String path) throws IOException {
        FileCreator fileCreator = new FileCreator(new LineReader());
        fileCreator.createLogFiles(path);

        FileMerger fileMerger = new FileMerger();
        fileMerger.merge(path);

//        FileDeletor fileDeletor = new FileDeletor();
//        fileDeletor.delete(path);
//
//        FileUpdator fileUpdator = new FileUpdator();
//        fileUpdator.update(path);
    }
}
