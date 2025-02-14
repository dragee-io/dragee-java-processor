package io.dragee.rules.fixes.issue9;

import java.util.UUID;

@Namespace.ChildType
record TestId(String value) {

    static TestId of(String value) {
        return new TestId(value);
    }

    static TestId generate() {
        return new TestId(UUID.randomUUID().toString());
    }
}
