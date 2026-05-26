package com.geekyhim.shortify.util;

import java.security.SecureRandom;

public class Base62Encoder {

    private static final String BASE62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom random = new SecureRandom();

    private static final int RANDOM_PART_LENGTH = 2;

    public static String encode(long value) {

        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }

        return sb.reverse().toString();
    }

    public static String generateRandomPrefix() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return sb.toString();
    }
}