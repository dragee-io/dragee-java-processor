package io.dragee.model;

public record Kind(String value) {

    public static final String SEGMENT = "/";

    public static Kind of(String value) {
        return new Kind(value);
    }
}
