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


    @Dragee.Namespace
    @SomeNamespace
    @Target({ElementType.TYPE})
    @interface SomeSubNamespace {
        @SomeSubNamespace
        @Target({ElementType.TYPE})
        @interface SomeOtherConcept {
        }
    }

    @Dragee.Namespace
    @SomeNamespace
    @Target({ElementType.TYPE})
    @interface SomeOtherSubNamespace{
        @Dragee.Namespace
        @SomeOtherSubNamespace
        @Target({ElementType.TYPE})
        @interface ASubSubNamespace{
            @ASubSubNamespace
            @Target({ElementType.TYPE})
            @interface SomeDrageeInSubSubNamespace{
            }
        }
    }
}
