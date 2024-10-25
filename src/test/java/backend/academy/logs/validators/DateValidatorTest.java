package backend.academy.logs.validators;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateValidatorTest {

    private final Validator validator = new DateValidator();

    static Stream<Arguments> dateTestCases() {
        return Stream.of(
            Arguments.of("2023-10-25", true),
            Arguments.of("2000-01-01", true),
            Arguments.of("1999-12-31", true),
            Arguments.of("2024-02-29", true),
            Arguments.of("1980-05-15", true),
            Arguments.of("23-10-25", false),
            Arguments.of("2023-1-01", false),
            Arguments.of("2023-10-5", false),
            Arguments.of("2023-13-01", false),
            Arguments.of("2023-00-10", false),
            Arguments.of("2023-10-32", false),
            Arguments.of("2023/10/25", false),
            Arguments.of("20231025", false),
            Arguments.of("abcd-ef-gh", false),
            Arguments.of("", false)
        );
    }

    @ParameterizedTest
    @MethodSource("dateTestCases")
    void testDateValidator(String date, boolean expected) {
        assertEquals(expected, validator.validate(date));
    }
}
