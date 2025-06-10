import utils.LogLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogTimeExtractor {
    public static void main(String[] args) {
        String log = LogLoader.loadLog("todo.log");
        List<Double> times = new ArrayList<>();

        Pattern pattern = Pattern.compile("INFO.*POST.*?(\\d+\\.\\d+)(ms|µs)");
        Matcher matcher = pattern.matcher(log);

        while (matcher.find()) {
            String value = matcher.group(1);
            String unit = matcher.group(2);

            if (unit.equals("ms")) {
                times.add(Double.parseDouble(value) * 1000);
            } else {
                times.add(Double.parseDouble(value));
            }
        }

        PostPerformanceTest.printPerformanceData(times);
    }
}
