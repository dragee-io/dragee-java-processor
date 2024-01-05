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
 *   // The name of a dragee is determined by annotation qualified name.
 *   //     Here, it will be "my_custom_dragee"
 *   // The kind of a dragee is a concatenation of the dragee namespace and the dragee name
 *   //     Here, it will be "my_custom_namespace/my_custom_dragee"
 *   @Dragee(namespace = "myCustomNamespace")
 *   public @interface MyCustomDragee{}
 *
 *   // This object will be a dragee of kind "my_custom_namespace/my_custom_dragee"
 *   @MyCustomDragee
 *   public class SomeObject {}
 *  }
 * </pre>
 *
 * Leaf dragee can be removed from compilation by using the retention policy "SOURCE".
 *
 * Be aware that if you're using extensions (a dragee that extend an other dragee), retention policy "RUNTIME"
 *  is currently necessary to make it works. So be careful when you're using this feature.
 *  This should be used only to group concepts under a same pattern.
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
 *   // note: DDD must have retention policy RUNTIME
 *   @DDD
 *   public @interface ValueObject{} {}
 *  }
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
    @Retention(RetentionPolicy.RUNTIME)
    @interface Namespace {
    }
}
