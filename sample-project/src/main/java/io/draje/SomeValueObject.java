package io.draje;

import io.draje.annotation.ValueObject;

@ValueObject
public class SomeValueObject {

    String aField;

    SomeValueObject(boolean inConstructor) {}

    Integer compute(Long a, Float b) {
        return 0;
    }

    static byte test() {
        return 0;
    }

}
