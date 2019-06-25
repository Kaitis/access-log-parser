package com.ef.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult implements Serializable {

    private static final long serialVersionUID = 482L;

    private String ip;

    private Integer total;

}
