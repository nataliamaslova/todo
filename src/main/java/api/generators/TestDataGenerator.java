package api.generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.util.Random;

public class TestDataGenerator {
    private static final Random random = new Random();
    private static final String TEST_PREFIX = "test_";
    private static final int MAX_LENGTH = 10;

    public static <T> T generate(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                Class<?> type = field.getType();

//                public static String getString(int length) {
//                    return TEST_PREFIX + RandomStringUtils
//                            .randomAlphabetic(Math.max(length - TEST_PREFIX.length(), MAX_LENGTH));
//                }

                if (type == String.class) {
                    field.set(instance, TEST_PREFIX + RandomStringUtils.randomAlphabetic(MAX_LENGTH));
                } else if (type == long.class || type == Long.class) {
                    field.set(instance, random.nextLong(100));
                } else if (type == boolean.class || type == Boolean.class) {
                    field.set(instance, random.nextBoolean());
                } else {
                    // optionally handle other types or nested classes
                    System.out.println("Unsupported type: " + type.getName());
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate test data", e);
        }
    }
}
