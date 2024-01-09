package io.dragee.annotation.ddd;

import io.dragee.annotation.Dragee;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Dragee.Namespace
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DDD {

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Aggregate {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Entity {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Command {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Event {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface ValueObject {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Factory {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Service {
    }

    @DDD
    @Documented
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    @interface Repository {
    }
}
