package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class LogLoader {
    public static String loadLog(String filename) {
        InputStream is = LogLoader.class.getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            throw new RuntimeException("File not found: " + filename);
        }

        return new BufferedReader(new InputStreamReader(is))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
