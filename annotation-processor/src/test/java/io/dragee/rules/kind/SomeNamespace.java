package io.dragee.rules.kind;

import io.dragee.annotation.Dragee;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Dragee.Namespace
@Target({ElementType.TYPE})
public @interface SomeNamespace {


    @Dragee.Namespace
    @SomeNamespace
    @Target({ElementType.TYPE})
    public @interface SomeSubNamespace {

        @SomeSubNamespace
        @Target({ElementType.TYPE})
        @interface SomeConcept {
        }

    }


}
