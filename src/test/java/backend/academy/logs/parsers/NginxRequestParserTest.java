package backend.academy.logs.parsers;

import backend.academy.logs.entities.Request;
import backend.academy.logs.types.RequestType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class NginxRequestParserTest {
    private final RequestParser requestParser = new NginxRequestParser();

    private static Stream<Arguments> provideValidRequests() {
        return Stream.of(
            Arguments.of("GET /home HTTP/1.1", RequestType.GET, "/home", "HTTP/1.1"),
            Arguments.of("POST /api/data HTTP/2.0", RequestType.POST, "/api/data", "HTTP/2.0"),
            Arguments.of("DELETE /resource/123 HTTP/1.0", RequestType.DELETE, "/resource/123", "HTTP/1.0"),
            Arguments.of("PUT /users/update HTTP/1.1", RequestType.PUT, "/users/update", "HTTP/1.1"),
            Arguments.of("HEAD /status HTTP/1.1", RequestType.HEAD, "/status", "HTTP/1.1")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidRequests")
    void testParseValidRequests(String request, RequestType expectedMethod, String expectedRout, String expectedHttpVersion) {
        Request parsedRequest = requestParser.parse(request);

        assertNotNull(parsedRequest);
        assertEquals(expectedMethod, parsedRequest.requestType());
        assertEquals(expectedRout, parsedRequest.rout());
        assertEquals(expectedHttpVersion, parsedRequest.httpVersion());
    }

    private static Stream<Arguments> provideInvalidRequests() {
        return Stream.of(
            Arguments.of("GET/home HTTP/1.1"),
            Arguments.of("FETCH /home HTTP/1.1"),
            Arguments.of("POST /home"),
            Arguments.of("/home HTTP/1.1"),
            Arguments.of("GET HTTP/1.1"),
            Arguments.of("GET /home HTTP/"),
            Arguments.of("GET  HTTP/1.1"),
            Arguments.of("POST /api/data HTTP/1.abc")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRequests")
    void testParseInvalidRequests(String request) {
        assertNull(requestParser.parse(request));
    }
}
