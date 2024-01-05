package io.dragee.util;

public class SnakeCase {

    private SnakeCase() {}

    public static String toSnakeCase(String strToConvert) {
        if (strToConvert == null || strToConvert.isBlank()) {
            return "";
        }

        return strToConvert
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase();
    }
}
