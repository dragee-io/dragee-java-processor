package io.dragee.rules.sub_namespace;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Foo
@Dragee.Namespace
@Target({ElementType.TYPE})
public @interface Bar {
}
