package com.example.demo;

import org.h2.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DemoApplication {
	private static final String SECOND_FILE_NAME_START_DELIMETER = "thread_id=";
	private static final String SECOND_FILE_NAME_END_DELIMETER = " ";
	private static final String EMPTY_STRING = "";


	private static final String FIRST_FILE_NAME_START_DELIMETER = "step";
	private static final String FIRST_FILE_NAME_END_DELIMETER = "]";



	private static String getThreadId(String line) {
		// thread id값만 얻기 위해 length를 더한다. thread_id=1로 기록되어 있으면 1의 값만 얻는다.
		int threadIdStartIndex = line.indexOf( SECOND_FILE_NAME_START_DELIMETER );
		int threadIdEndIndex = line.indexOf(SECOND_FILE_NAME_END_DELIMETER, threadIdStartIndex);

		if (threadIdStartIndex == -1 || threadIdEndIndex == -1) {
			return EMPTY_STRING;
		}

		return line.substring(threadIdStartIndex + SECOND_FILE_NAME_START_DELIMETER.length(), threadIdEndIndex);
	}

	private static String getStepId(String line) {
		// thread id값만 얻기 위해 length를 더한다. thread_id=1로 기록되어 있으면 1의 값만 얻는다.
		int stepIdStartIndex = line.indexOf( FIRST_FILE_NAME_START_DELIMETER );
		int stepIdEndIndex = line.indexOf( FIRST_FILE_NAME_END_DELIMETER, stepIdStartIndex);

		if (stepIdStartIndex == -1 || stepIdEndIndex == -1) {
			return EMPTY_STRING;
		}

		return line.substring(stepIdStartIndex + FIRST_FILE_NAME_START_DELIMETER.length(), stepIdEndIndex);
	}


	private static void writeLine(String line, String targetPath) throws IOException {
		BufferedOutputStream bufferedOutputStream = null;

		try {
			byte[] data;
			String lineSeparator = System.getProperty("line.separator");

			targetPath = targetPath;
			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetPath, true));

			data = line.getBytes();
			bufferedOutputStream.write(data);
			bufferedOutputStream.write(lineSeparator.getBytes());
			bufferedOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bufferedOutputStream.close();
		}
	}

	public static void main(String[] args) throws IOException {

		String path = "C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test";

		File folder = new File(path);
		File[] files = folder.listFiles();

		BufferedOutputStream bufferedOutputStream = null;
		BufferedReader bufferedReader = null;
		byte[] data; String line;
		int count = 0;
		String lineSeparator = System.getProperty("line.separator");
		String targetPath = "C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test\\step1 1";

		try {
			// 파일 생성
			for (File file : files) {
				if (file.exists() && file.isFile()) {
					bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

					while (  (line = bufferedReader.readLine()) != null  ) {
						String stepId = getStepId(line);
						String threadId = getThreadId(line);

						if (!StringUtils.isNullOrEmpty(threadId)) {
							targetPath = "C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test\\" + threadId + ".log";

							if (!StringUtils.isNullOrEmpty(stepId)) {
								targetPath = "C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test\\" + stepId + " " + threadId + ".log";
							}
						}

						writeLine(line, targetPath);
					}
					bufferedReader.close();
				}
			}




			// 파일 합치기
			targetPath = "C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test\\allinone.log";

			path = "C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test";
			folder = new File(path);
			File[] forderList = folder.listFiles();

			Arrays.sort(forderList, new Comparator()
			{
				@Override
				public int compare(Object f1, Object f2) {
					String fileName1 = ((File) f1).getName().split("[.]")[0];
					String fileName2 = ((File) f2).getName().split("[.]")[0];

					String[] weStringSet = fileName1.split(" ");
					String[] thereStringSet = fileName2.split(" ");

					if (weStringSet.length == 1) return -1;
					if (thereStringSet.length == 1) return 1;

					int weNumber = Integer.parseInt(weStringSet[0]);
					int thereNumber = Integer.parseInt(thereStringSet[0]);

					return weNumber - thereNumber;
				}
			});

			for (File file : forderList) {
				if (file.exists() && file.isFile()) {
					if (file.getName().equals("sample.log")) continue;

					if (bufferedOutputStream != null) bufferedOutputStream.flush();
					bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetPath, true));

					System.out.println("fileNm : ".concat(file.toString().toUpperCase()));

					bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

					while (  (line = bufferedReader.readLine()) != null  ) {
						data = line.getBytes();
						bufferedOutputStream.write(data);
						bufferedOutputStream.write(lineSeparator.getBytes());
					}

					count++;
				}
				bufferedReader.close();
			}
			bufferedOutputStream.close();





			// 파일 삭제
			for (File file : forderList) {
				if (file.exists() && file.isFile()) {
					if (file.getName().equals("allinone.log")) continue;

					boolean boo = file.delete();

					count++;
				}
			}




			// 파일 대체
			for (File file : forderList) {
				if (file.exists() && file.isFile()) {
					if (file.getName().equals("allinone.log")) {
						file.renameTo(new File("C:\\Users\\USER\\Desktop\\정산\\metering_batch\\logs\\test\\sample.log"));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("222222222222222222");

			e.printStackTrace();
		} finally {
			System.out.println("555555555555555555555");
			bufferedReader.close();
			bufferedOutputStream.close();
		}



	}
}
