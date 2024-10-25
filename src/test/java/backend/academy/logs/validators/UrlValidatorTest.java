package backend.academy.logs.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {

    private final Validator lpv = new UrlValidator();

    private static Stream<Arguments> provideDataForValidate() {
        return Stream.of(
            Arguments.of("http://example.com", true),
            Arguments.of("https://subdomain.example.com/path/to/resource", true),
            Arguments.of("ftp://example.com:21", true),
            Arguments.of("http://example.com/path", true),
            Arguments.of("https://example.com:8080/resource?query=param", true),
            Arguments.of("http://.com", false),
            Arguments.of("http://example:port", false),
            Arguments.of("ftp://example..com", false),
            Arguments.of("http://", false),
            Arguments.of("https://example.com:abc", false),
            Arguments.of("https://raw.githubusercontent.com/elastic/examples/master/" +
                "Common%20Data%20Formats/nginx_logs/nginx_logs", true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForValidate")
    void validate(String path, boolean expected) {
        boolean actual = lpv.validate(path);
        assertEquals(expected, actual);
    }
}
