package io.dragee.rules.kind;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Dragee.Namespace
@Target({ElementType.TYPE})
public @interface SomeOtherNamespace {

        @SomeOtherNamespace
        @Target({ElementType.TYPE})
        @interface SomeOtherConcept {
        }

}
