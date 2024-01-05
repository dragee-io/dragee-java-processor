package io.dragee.testing;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Dragee
@Inherited
@Target(ElementType.TYPE)
public @interface KindOne {
}
