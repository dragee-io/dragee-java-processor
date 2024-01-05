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
 * It can be extended by creating annotations that are annotated with {@link Dragee}.
 *
 * Example:
 * <pre>
 *  {@code
 *   @Dragee
 *   public @interface MyCustomDragee{}
 *  }
 * </pre>
 *
 * The name of the dragee follow snake case rules, must be unique across all dragees.
 * For java, the name of a dragee is determined by the annotation qualified name.
 *
 * Example: MyCustomDragee becomes a dragee with name my_custom_dragee.
 */
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dragee {

    @Deprecated
    String value() default "";
}
