package io.dragee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In Domain-Driven-Design, we may want to show the tactical patterns such as Aggregates or Value objects.
 * We may also want to show architectural design, like domain / infra layer for hexagonal.
 * Basically, everything we would like to show, can be shown.
 *
 * Dragee is covering those cases by using annotations directly on what we want to highlight.
 * It can be extended by creating annotations that are annotated with {@link Dragee.Namespace} and then by annotating classes.
 *
 * <pre>
 *  {@code
 *   // The namespace is determined by the annotation qualified name.
 *   //     Here, it will be "ddd"
 *   @Dragee.Namespace
 *   public @interface DDD{}
 *
 *   // The name of a dragee is determined by the annotation qualified name.
 *   //     Here, it will be "value_object"
 *   // The kind of a dragee is a concatenation of the dragee namespace and the dragee name
 *   //     Here, it will be "ddd/value_object"
 *   @DDD
 *   public @interface ValueObject{} {}
 *  }
 *
 *  Namespace, name and kind follow the snake_case convention.
 *
 *  Note that namespaces annotation can not have retention policy "SOURCE", but dragees can.
 * </pre>
 */
@Documented
@Inherited
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface Dragee {

    /**
     *  A dragee must be under a namespace in order to distinguish two identical names from different context
     *  Some example of namespaces: "ddd", "cqrs", "hexagonal".
     */
    @Documented
    @Inherited
    @Target({ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.CLASS)
    @interface Namespace {
    }
}
