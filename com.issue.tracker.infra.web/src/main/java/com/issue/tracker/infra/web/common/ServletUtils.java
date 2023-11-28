package com.issue.tracker.infra.web.common;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtils {
    public static String readBody(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
