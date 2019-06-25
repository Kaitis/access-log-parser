package com.ef.parser.enums;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
public enum Duration {
    DAILY (500),
    HOURLY (200);

    private final int value;

    Duration(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
