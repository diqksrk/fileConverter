package com.example.demo.model;

import com.example.demo.utils.NumberUtils;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
public class LineReader {
    private static final String SECOND_FILE_NAME_START_DELIMETER = "thread_id=";
    private static final String SECOND_FILE_NAME_END_DELIMETER = " ";
    private static final String FIRST_FILE_NAME_START_DELIMETER = "step";
    private static final String FIRST_FILE_NAME_END_DELIMETER = "]";
    private static final String EMPTY_STRING = "";

    private String readStepId;
    private String readThreadId;
    private String readSequenceNumber;


    public String getStepId(String line) {
        // thread id값만 얻기 위해 length를 더한다. thread_id=1로 기록되어 있으면 1의 값만 얻는다.
        int stepIdStartIndex = line.indexOf( FIRST_FILE_NAME_START_DELIMETER );
        int stepIdEndIndex = line.indexOf( FIRST_FILE_NAME_END_DELIMETER, stepIdStartIndex);

        if (stepIdStartIndex == -1 || stepIdEndIndex == -1) {
            return EMPTY_STRING;
        }

        return readStepId = line.substring(stepIdStartIndex + FIRST_FILE_NAME_START_DELIMETER.length(), stepIdEndIndex);
    }

    public String getThreadId(String line) {
        String[] logStrings = line.split(" ");

        // thread id값만 얻기 위해 length를 더한다. thread_id=1로 기록되어 있으면 1의 값만 얻는다.
        int threadIdStartIndex = line.indexOf( SECOND_FILE_NAME_START_DELIMETER );
        int threadIdEndIndex = line.indexOf(SECOND_FILE_NAME_END_DELIMETER, threadIdStartIndex);

        if (threadIdStartIndex == -1 || threadIdEndIndex == -1) {
            return EMPTY_STRING;
        }

        setSequenceNumber(line);

        return readThreadId = line.substring(threadIdStartIndex + SECOND_FILE_NAME_START_DELIMETER.length(), threadIdEndIndex);
    }

    private void setSequenceNumber(String line) {
        int firstIndex = line.indexOf("(");
        int secondIndex = line.indexOf(")");

        if (NumberUtils.isNumeric(line.substring(firstIndex + 1, secondIndex))) {
            readSequenceNumber = line.substring(firstIndex + 1, secondIndex);
        }
    }

    public String getSequenceNumber() {
        return this.readSequenceNumber;
    }

    public String getPrevTargetPath() {
        if (StringUtils.isBlank(readStepId) && StringUtils.isBlank(readSequenceNumber)) {
            return "1";
        }

        if ( StringUtils.isBlank(readStepId) ) {
            return this.readThreadId + " " + getSequenceNumber();
        }

        return this.getSequenceNumber() + " " + this.readThreadId;
    }
}
