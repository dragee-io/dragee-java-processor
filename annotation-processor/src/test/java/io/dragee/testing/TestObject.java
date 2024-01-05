package io.dragee.testing;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Dragee
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface TestObject {
}
