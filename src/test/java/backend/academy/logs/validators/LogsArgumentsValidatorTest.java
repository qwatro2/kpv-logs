package backend.academy.logs.validators;

import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.entities.ValidationResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogsArgumentsValidatorTest {

    private final ArgumentsValidator validator = new LogsArgumentsValidator(
        new LocalPathValidator(),
        new UrlPathValidator(),
        new DateValidator()
    );

    static Stream<Arguments> provideParsingResults() {
        return Stream.of(
            Arguments.of(createParsingResult(null, "2023-10-01", "2023-10-31", "markdown", null, null), false),
            Arguments.of(createParsingResult("invalidPath.txt", "2023-10-01", "2023-10-31", "markdown", null, null), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "markdown", null, null), true),
            Arguments.of(createParsingResult("http://example.com/logs/file.md", "2023-10-01", "2023-10-31", "markdown", null, null), true),
            Arguments.of(createParsingResult("/valid/path/file.md", null, "2023-10-31", "markdown", null, null), true),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", null, "markdown", null, null), true),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "invalid-date", "markdown", null, null), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "invalid-date", "2023-10-31", "markdown", null, null), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "unsupported", null, null), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "markdown", null, null), true),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "adoc", null, null), true),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "markdown", "remote-address", null), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "markdown", null, "value"), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "markdown", "invalid-field", "value"), false),
            Arguments.of(createParsingResult("/valid/path/file.md", "2023-10-01", "2023-10-31", "markdown", "remote-address", "127.0.0.1"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParsingResults")
    void testValidationResult(ParsingResult parsingResult, boolean expectedIsValid) {
        ValidationResult validationResult = validator.validate(parsingResult);
        assertEquals(expectedIsValid, validationResult.isValid());
    }

    private static ParsingResult createParsingResult(String path, String from, String to,
        String format, String filterField, String filterValue) {
        return new ParsingResult()
            .path(path)
            .from(from)
            .to(to)
            .format(format)
            .filterField(filterField)
            .filterValue(filterValue);
    }

    static Stream<Arguments> provideOutputValidationParsingResults() {
        return Stream.of(
            Arguments.of(createParsingResult(null), true),
            Arguments.of(createParsingResult(""), false),
            Arguments.of(createParsingResult("./dir"), false),
            Arguments.of(createParsingResult("./result.txt"), true),
            Arguments.of(createParsingResult("./**/result.txt"), false),
            Arguments.of(createParsingResult("C:/**/result.txt"), false),
            Arguments.of(createParsingResult("./dir/*.txt"), false),
            Arguments.of(createParsingResult("C:/dir/*.txt"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOutputValidationParsingResults")
    void testOutputValidation(ParsingResult parsingResult, boolean expectedIsValid) {
        ValidationResult validationResult = validator.validate(parsingResult);
        assertEquals(expectedIsValid, validationResult.isValid());
    }

    private static ParsingResult createParsingResult(String output) {
        return createParsingResult("/valid/path/file.md", null, null, null, null, null).output(output);
    }
}
