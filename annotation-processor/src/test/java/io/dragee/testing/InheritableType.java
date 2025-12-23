package io.dragee.testing;

import io.dragee.annotation.Inheritable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Testing
@Inheritable
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface InheritableType {
}
