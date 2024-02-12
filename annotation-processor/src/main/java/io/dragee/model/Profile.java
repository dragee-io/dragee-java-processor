package io.dragee.model;

public record Profile(String value) {

    public static Profile of(String value) {
        return new Profile(value);
    }
}
