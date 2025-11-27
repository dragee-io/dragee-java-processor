package io.dragee.annotation.clean;

import io.dragee.annotation.Dragee;

import java.lang.annotation.*;

@Dragee.Namespace
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Clean {

    @Clean
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Controller {
    }

    @Clean
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Presenter {
    }

    @Clean
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface UseCase {
    }
}
