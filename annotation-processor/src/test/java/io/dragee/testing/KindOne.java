package io.dragee.testing;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Dragee("kind_one")
@Target({ElementType.TYPE})
public @interface KindOne {
}
