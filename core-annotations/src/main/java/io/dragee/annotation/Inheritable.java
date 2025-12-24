package io.dragee.annotation;

import java.lang.annotation.*;

/**
 * Indicates that an annotation should be inherited by subclasses.
 */
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Inheritable {
}
