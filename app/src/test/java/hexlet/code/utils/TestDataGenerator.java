package hexlet.code.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class TestDataGenerator {

    public static String randomLogin() {
        return "user" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    public static String randomPassword() {
        return "pass" + ThreadLocalRandom.current().nextInt(10000, 99999);
    }
}