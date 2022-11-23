package com.example.demo;

import com.example.demo.controller.FileController;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DemoApplication {
	private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static String makeJenkinsBuildPath(String[] args) {
//		StringBuffer stringLine = new StringBuffer();
//
//		if (args.length > 0) {
//			stringLine.append(args[0]);
//		}
//		if (args.length > 1) {
//			stringLine.append(" ");
//			stringLine.append(args[1]);
//		}
//		if (args.length > 2) {
//			stringLine.append(" ");
//			stringLine.append(args[2]);
//		}
//		return stringLine.toString();

		return "C:/Users/diqks/Desktop/이거/";
	}
	public static void main(String[] args) throws IOException {
		String targetPath = makeJenkinsBuildPath(args);

		ThreadContext.put("fileCreatePosition", targetPath + "jenkins_file_update/log");

		logger.info("targetPahth : " + targetPath);
		logger.info("fileCreatePosition : " + targetPath + "jenkins_file_update/log");

		FileController fileController = new FileController();
		fileController.generate(targetPath);
	}
}