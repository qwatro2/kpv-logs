package backend.academy.logs.parsers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class NginxLocalDatetimeParserTest {
    private final LocalDatetimeParser parser = new NginxLocalDatetimeParser();

    private static Stream<Arguments> provideValidDateTimes() {
        return Stream.of(
            Arguments.of("12/Feb/2022:14:23:45 +0000", LocalDateTime.of(2022, 2, 12, 14, 23, 45)),
            Arguments.of("01/Jan/2023:00:00:00 +0000", LocalDateTime.of(2023, 1, 1, 0, 0, 0)),
            Arguments.of("31/Dec/2021:23:59:59 +0200", LocalDateTime.of(2021, 12, 31, 23, 59, 59)),
            Arguments.of("15/Mar/2020:12:30:25 -0500", LocalDateTime.of(2020, 3, 15, 12, 30, 25))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidDateTimes")
    void testParseValidDateTimes(String dateTime, LocalDateTime expected) {
        assertEquals(expected, parser.parse(dateTime));
    }

    private static Stream<Arguments> provideInvalidDateTimes() {
        return Stream.of(
            Arguments.of("32/Jan/2022:14:23:45 +0000"), // invalid day
            Arguments.of("12/Fake/2022:14:23:45 +0000"), // invalid month
            Arguments.of("12/Feb/2022 14:23:45 +0000"), // missing colon after date
            Arguments.of("12/Feb/2022:14:23:99 +0000"), // invalid second
            Arguments.of("29/Feb/2021:12:30:45 +0000"), // 2021 is not a leap year
            Arguments.of("12/Feb/2022:25:00:00 +0000"), // invalid hour
            Arguments.of("12/Feb/2022:14:61:00 +0000") // invalid minute
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDateTimes")
    void testParseInvalidDateTimes(String dateTime) {
        assertNull(parser.parse(dateTime));
    }
}
