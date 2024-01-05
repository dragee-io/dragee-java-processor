package io.dragee.util;

public class SimpleName {

    private SimpleName() {}

    public static String toSimpleName(String name) {
        int lastIndex = name.lastIndexOf(".");
        int beginningOfSimpleName = lastIndex == -1 ? 0 : lastIndex + 1;

        return name.substring(beginningOfSimpleName);
    }

}
