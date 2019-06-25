package com.ef.parser.utils;

import java.io.File;

/**
 * Created by andreaskaitis on 2019-06-25.
 * parser-wallethub
 */
public class FileUtils {

    public static File existsAndReadable(String path) throws IllegalArgumentException {
        File file = new File(path);

        if (!file.exists()) {

            throw new IllegalArgumentException("File not found, make sure that the path exists: " + path);

        } else {

            if (!file.canRead()) {
                throw new IllegalArgumentException("Can not read data from file: " + file.getName());
            }

            return file;

        }
    }
}
