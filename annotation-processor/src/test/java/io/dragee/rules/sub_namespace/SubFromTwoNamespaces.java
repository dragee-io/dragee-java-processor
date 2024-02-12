package io.dragee.rules.sub_namespace;

import io.dragee.annotation.Dragee;
import io.dragee.testing.Testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Testing
@Foo
@Dragee.Namespace
@Target({ElementType.TYPE})
public @interface SubFromTwoNamespaces {
}
