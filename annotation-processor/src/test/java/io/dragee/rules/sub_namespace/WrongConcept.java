package io.dragee.rules.sub_namespace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@SubFromTwoNamespaces
@Target({ElementType.TYPE})
@interface WrongConcept {
}
