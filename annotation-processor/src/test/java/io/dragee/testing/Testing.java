package io.dragee.testing;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Dragee.Namespace
@Target({ElementType.ANNOTATION_TYPE})
public @interface Testing {
}
