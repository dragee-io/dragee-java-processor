package io.dragee.rules.ddd.sample;

import io.dragee.annotation.ddd.ValueObject;

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
