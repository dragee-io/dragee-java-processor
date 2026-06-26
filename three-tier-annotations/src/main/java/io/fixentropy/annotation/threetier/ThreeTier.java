package io.fixentropy.annotation.threetier;

import io.fixentropy.annotation.Fixentropy;

import java.lang.annotation.*;

@Fixentropy.Namespace
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreeTier {

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Entity {
    }

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface RequestModel {
    }

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ResponseModel {
    }

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Service {
    }

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Repository {
    }

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Presenter {
    }

    @ThreeTier
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Mapper {
    }
}