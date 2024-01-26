package io.dragee.annotation.clean;

import io.dragee.annotation.Dragee;

import java.lang.annotation.*;

@Dragee.Namespace
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Clean {

    @Clean
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Controller {
    }

    @Clean
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Presenter {
    }

    @Clean
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface UseCase {
    }
}
