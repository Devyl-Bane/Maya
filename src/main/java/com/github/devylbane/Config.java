package com.github.devylbane;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{
    public static final String PREFIX;
    static final String TOKEN;

    static
    {
        String tempToken, tempPrefix;
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.properties"));
            tempToken = unWrap(props.getProperty("token"), "\"");
            tempPrefix = unWrap(props.getProperty("prefix"), "\"");
        } catch (IOException e) {
            tempToken = "not today";
            tempPrefix = "+";
        }
        TOKEN = tempToken;
        PREFIX = tempPrefix;
    }

    private static String unWrap(String src, String seq)
    {
        if (src.startsWith(seq) && src.endsWith(seq))
            return src.substring(seq.length(), src.length() - seq.length());
        return src;
    }
}
