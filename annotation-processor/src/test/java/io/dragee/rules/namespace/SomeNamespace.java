package io.dragee.rules.namespace;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Dragee.Namespace
@Target({ElementType.TYPE})
public @interface SomeNamespace {

    @SomeNamespace
    @Target({ElementType.TYPE})
    @interface SomeConcept {
    }

}
