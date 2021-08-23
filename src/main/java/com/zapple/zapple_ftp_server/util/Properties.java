package com.zapple.zapple_ftp_server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class Properties {
    private static final Logger logger = LoggerFactory.getLogger(Properties.class);
    private static String HASH = "#";
    private static String EQUAL = "=";

    private List<String> keyList = new LinkedList<>();

    private Map<String, String> valueMap = new LinkedHashMap<>();

    public Map<String, String> getPropertyMap() {
        return valueMap;
    }

    public String getProperty(String key) {
        return valueMap.get(key);
    }

    public void setProperty(String key, String value) {
        if(!valueMap.containsKey(key)) {
            keyList.add(key);
        }
        valueMap.put(key, value);
    }

    public void removeProperty(String key) {
        if(valueMap.containsKey(key)) {
            keyList.removeIf(o -> o.equals(key));
            valueMap.remove(key);
        }
    }

    public synchronized  void load(InputStream inputStream) throws IOException {
        keyList.clear();
        valueMap.clear();

        try {
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                readLine(bufferedReader.readLine());
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
        }
    }

    private void readLine(String line) {
        if(!line.trim().startsWith(HASH) && line.contains(EQUAL)) {
            String k = line.substring(0, line.indexOf(EQUAL)).trim();
            String v = line.substring(line.indexOf(EQUAL)+1).trim();
            valueMap.put(k,v);
            keyList.add(k);
        } else {
            keyList.add(line);
        }
    }

    public void store(OutputStream outputStream) {
        try {
            Writer writer = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for(String key : keyList) {
                if(valueMap.containsKey(key)) {
                    bufferedWriter.write(key + EQUAL + valueMap.get(key));
                } else {
                    bufferedWriter.write(key);
                }
                bufferedWriter.newLine();
            }
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    public Set<String> keySet() {
        return valueMap.keySet();
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return valueMap.entrySet();
    }

    @Override
    public String toString() {
        return valueMap.toString();
    }
}
