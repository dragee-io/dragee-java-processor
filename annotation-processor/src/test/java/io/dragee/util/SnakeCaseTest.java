package io.dragee.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SnakeCaseTest {

    @ParameterizedTest
    @NullAndEmptySource
    void empty_string(String nullOrEmpty) {
        String actual = SnakeCase.toSnakeCase(nullOrEmpty);

        assertThat(actual).isEqualTo("");
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "\t \s" })
    void blank_becomes_empty(String blank) {
        String actual = SnakeCase.toSnakeCase(blank);

        assertThat(actual).isEqualTo("");
    }

    @ParameterizedTest
    @MethodSource("examples")
    void convert_to_snake_case(String given, String expected) {
        String actual = SnakeCase.toSnakeCase(given);

        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> examples() {
        return Stream.of(
                arguments("PascalCase", "pascal_case"),
                arguments("path.to.FooBar", "path.to.foo_bar"));
    }
}