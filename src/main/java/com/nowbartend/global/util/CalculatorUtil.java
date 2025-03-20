package com.nowbartend.global.util;

public class CalculatorUtil {

    public static int ceilToNearestTenThousand(int price) {
        return (int) Math.ceil(price / 10000.0) * 10000;
    }
}
