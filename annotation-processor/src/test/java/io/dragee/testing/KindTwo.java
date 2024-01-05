package io.dragee.testing;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Dragee
@Target(ElementType.TYPE)
public @interface KindTwo {
}
