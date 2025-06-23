import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostPerformanceTest extends BaseTest {

    @Test
    public void userCanEstimatePerformanceForPOST() {
        List<Double> times = new ArrayList<>();
        for (int i = 0; i < REQUESTS_COUNT; i++) {
            todo.setId(i);
            todo.setText("test_" + i);
            long start = System.nanoTime();
            todoService.create(todo, HttpStatus.SC_CREATED);
            long end = System.nanoTime();
            times.add((end - start) / 1000.0);
        }

        printPerformanceData(times);
    }

    public static void printPerformanceData(List<Double> times) {
        System.out.println("Max POST time: " + times.stream().max(Comparator.naturalOrder()));
        System.out.println("Min POST time: " + times.stream().min(Comparator.naturalOrder()));
        System.out.println("Average POST time: " + times.stream().mapToDouble(n -> n).average());

        System.out.println("POST duration: " + times);
    }
}
