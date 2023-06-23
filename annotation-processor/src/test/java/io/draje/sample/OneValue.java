package io.draje.sample;

import io.draje.annotation.ValueObject;

@ValueObject
public class OneValue {

    String aField;

    OneValue(boolean inConstructor) {}

    Integer compute(Long a, Float b) {
        return 0;
    }

    static byte test() {
        return 0;
    }

}
