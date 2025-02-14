package io.dragee.rules.fixes.issue9;

import io.dragee.annotation.Dragee;

import java.lang.annotation.*;

@Dragee.Namespace
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Namespace {

    @Namespace
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ParentType {
    }

    @Namespace
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ChildType {
    }
}
