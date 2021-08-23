package com.zapple.zapple_ftp_server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PropertiesHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

    public static Properties getProperties(String inputFilePath) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(inputFilePath);
            properties.load(fileInputStream);
        } catch(IOException exception) {
            logger.error(exception.getMessage());
        }
        return properties;
    }

    public static void setProperties(Properties properties, String outputFilePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
            properties.store(fileOutputStream);
        } catch(IOException exception) {
            logger.error(exception.getMessage());
        }
    }
}
