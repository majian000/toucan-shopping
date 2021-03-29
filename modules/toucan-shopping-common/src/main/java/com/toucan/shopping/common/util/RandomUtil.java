package com.toucan.shopping.common.util;

import java.util.Random;

public class RandomUtil {
    private static final Random random= new Random();

    public static int nextInt(int number)
    {
        return random.nextInt(number);
    }

    public static String random(int length){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(String.valueOf(random.nextInt(10)));
        }
        return builder.toString();
    }
}
