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
 *   @Dragee("my_custom_dragee")
 *   public @interface MyCustomDragee{}
 *  }
 * </pre>
 */
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dragee {

    /**
     * @return the name of the dragee.
     *  It should be unique accross all dragees.
     *  Naming convention is lower+snake case.
     */
    String value();
}
