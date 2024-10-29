package backend.academy.logs.parsers;

import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.Request;
import backend.academy.logs.types.RequestType;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class NginxLogParserTest {
    private final LogParser<NginxLog> parser =
        new NginxLogParser(new NginxRequestParser(), new NginxLocalDatetimeParser());

    private static Stream<Arguments> provideValidLogs() {
        return Stream.of(
            Arguments.of(
                "127.0.0.1 -john- [12/Feb/2022:14:23:45 +0000] \"GET /home HTTP/1.1\" 200 1234 " +
                    "\"http://example.com\" \"Mozilla/5.0\"",
                new NginxLog("127.0.0.1", "john",
                    LocalDateTime.of(2022, 2, 12, 14, 23, 45),
                    new Request(RequestType.GET, "/home", "HTTP/1.1"),
                    200, 1234, "http://example.com", "Mozilla/5.0")
            ),
            Arguments.of(
                "192.168.1.1 -jane- [01/Jan/2023:00:00:00 +0000] \"POST /api/data HTTP/2.0\" 404 0 " +
                    "\"-\" \"curl/7.68.0\"",
                new NginxLog("192.168.1.1", "jane",
                    LocalDateTime.of(2023, 1, 1, 0, 0, 0),
                    new Request(RequestType.POST, "/api/data", "HTTP/2.0"),
                    404, 0, "-", "curl/7.68.0")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidLogs")
    void testParseValidLogs(String log, NginxLog expectedLog) {
        NginxLog parsedLog = parser.parse(log);
        assertNotNull(parsedLog);
        assertEquals(expectedLog, parsedLog);
    }

    private static Stream<Arguments> provideInvalidLogs() {
        return Stream.of(
            Arguments.of(
                "127.0.0.1 -john- [12/Feb/2022:14:23:45 +0000] \"UNKNOWN /home HTTP/1.1\" 200 1234 \"http://example.com\" \"Mozilla/5.0\""),
            Arguments.of(
                "127.0.0.1 -john- [12/Feb/2022:14:23:45 +0000] \"GET /home HTTP/1.1\" 200 1234 \"http://example.com\""),
            Arguments.of("127.0.0.1 -john- [12/Feb/2022:14:23:45 +0000] \"GET /home HTTP/1.1\" 200 1234"),
            Arguments.of(
                "127.0.0.1 - [12/Feb/2022:14:23:45 +0000] \"GET /home HTTP/1.1\" OK 1234 \"http://example.com\" \"Mozilla/5.0\""),
            Arguments.of(
                "127.0.0.1 -john- [12/Feb/2022:99:99:99 +0000] \"GET /home HTTP/1.1\" 200 1234 \"http://example.com\" \"Mozilla/5.0\""),
            Arguments.of(
                "127.0.0.1 -john- [12/Feb/2022:14:23:45] \"GET /home HTTP/1.1\" 200 1234 \"http://example.com\" \"Mozilla/5.0\"")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLogs")
    void testParseInvalidLogs(String log) {
        assertNull(parser.parse(log));
    }
}
