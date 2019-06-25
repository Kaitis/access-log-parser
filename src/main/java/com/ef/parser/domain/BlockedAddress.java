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
public class BlockedAddress implements Serializable {

    private static final long serialVersionUID = 123L;

    private String ip;

    private String comment;
}
