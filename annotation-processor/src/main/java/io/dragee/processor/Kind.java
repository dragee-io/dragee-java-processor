package io.dragee.processor;

public record Kind(String value) {

    public static Kind of(String value) {
        return new Kind(value);
    }

}
