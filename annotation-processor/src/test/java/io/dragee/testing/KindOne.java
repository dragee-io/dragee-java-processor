package io.dragee.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Testing
@Target({ElementType.TYPE})
public @interface KindOne {
}
