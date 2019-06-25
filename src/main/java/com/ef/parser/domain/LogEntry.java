package com.ef.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {

    private static final long serialVersionUID = 456L;

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private Date logDate;

    private String ip;

    private String request;

    private int status;

    private String userAgent;

    public void setLogDate(String logDate) throws Exception {
        this.logDate = FORMATTER.parse(logDate);
    }
}
