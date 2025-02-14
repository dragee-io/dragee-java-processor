package io.dragee.rules.fixes.issue9;

@Namespace.ParentType
record Test(TestId id) {

    Test {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    boolean hasId(TestId id) {
        return this.id.equals(id);
    }
}
