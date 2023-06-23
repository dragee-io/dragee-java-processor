package io.dragee.annotation.ddd;

import io.dragee.annotation.Dragee;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DDDAnnotationsTest {

    @ParameterizedTest
    @ArgumentsSource(DDDAnnotationsTest.DDDArgumentsSource.class)
    void extends_dragee_with_ddd_annotations(Class<? extends Annotation> annotation, String kindOfDragee) {
        assertThat(annotation.getAnnotation(Dragee.class))
                .isNotNull()
                .satisfies(dragee -> assertThat(dragee.value()).isEqualTo(kindOfDragee));
    }

    public static class DDDArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(Aggregate.class, "aggregate"),
                    Arguments.of(DomainCommand.class, "domain_command"),
                    Arguments.of(DomainEvent.class, "domain_event"),
                    Arguments.of(DomainFactory.class, "domain_factory"),
                    Arguments.of(DomainRepository.class, "domain_repository"),
                    Arguments.of(DomainService.class, "domain_service"),
                    Arguments.of(ValueObject.class, "value_object")
            );
        }
    }
}
