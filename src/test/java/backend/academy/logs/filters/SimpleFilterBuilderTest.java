package backend.academy.logs.filters;

import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.entities.Request;
import backend.academy.logs.types.RequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilterBuilderTest {

    private FilterBuilder filterBuilder;
    private ParsingResult parsingResult;
    private NginxLog logEntry;

    @BeforeEach
    void setUp() {
        filterBuilder = new SimpleFilterBuilder();
        parsingResult = mock(ParsingResult.class);
        logEntry = mock(NginxLog.class);
    }

    @Test
    void testProcessFrom() {
        when(parsingResult.from()).thenReturn("2023-10-01");
        LocalDate logDate = LocalDate.of(2023, 10, 1);
        when(logEntry.timeLocal()).thenReturn(LocalDateTime.of(logDate, LocalDateTime.now().toLocalTime()));

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.timeLocal()).thenReturn(LocalDateTime.of(2023, 9, 30, 12, 0));
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testProcessTo() {
        when(parsingResult.to()).thenReturn("2023-10-15");
        LocalDate logDate = LocalDate.of(2023, 10, 15);
        when(logEntry.timeLocal()).thenReturn(LocalDateTime.of(logDate, LocalDateTime.now().toLocalTime()));

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.timeLocal()).thenReturn(LocalDateTime.of(2023, 10, 16, 12, 0));
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testFilterByStatus() {
        when(parsingResult.filterField()).thenReturn("status");
        when(parsingResult.filterValue()).thenReturn("200");
        when(logEntry.status()).thenReturn(200);

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.status()).thenReturn(404);
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testFilterByBodyBytesSent() {
        when(parsingResult.filterField()).thenReturn("body-bytes-sent");
        when(parsingResult.filterValue()).thenReturn("512");
        when(logEntry.bodyBytesSent()).thenReturn(512);

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.bodyBytesSent()).thenReturn(1024);
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testFilterByRemoteAddress() {
        when(parsingResult.filterField()).thenReturn("remote-address");
        when(parsingResult.filterValue()).thenReturn("192.168.1.1");
        when(logEntry.remoteAddress()).thenReturn("192.168.1.1");

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.remoteAddress()).thenReturn("192.168.1.2");
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testFilterByRequestType() {
        when(parsingResult.filterField()).thenReturn("request-type");
        when(parsingResult.filterValue()).thenReturn("GET");
        Request request = mock(Request.class);
        when(request.requestType()).thenReturn(RequestType.GET);
        when(logEntry.request()).thenReturn(request);

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.request().requestType()).thenReturn(RequestType.POST);
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testFilterByTimeLocalExactMatch() {
        when(parsingResult.filterField()).thenReturn("time-local");
        when(parsingResult.filterValue()).thenReturn("2023-10-01T12:30:00");
        when(logEntry.timeLocal()).thenReturn(LocalDateTime.of(2023, 10, 1, 12, 30, 0));

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));

        when(logEntry.timeLocal()).thenReturn(LocalDateTime.of(2023, 10, 1, 12, 31, 0));
        assertFalse(filter.test(logEntry));
    }

    @Test
    void testUnsupportedField() {
        when(parsingResult.filterField()).thenReturn("unsupported-field");
        when(parsingResult.filterValue()).thenReturn("some-value");

        Predicate<NginxLog> filter = filterBuilder.build(parsingResult);
        assertTrue(filter.test(logEntry));
    }
}
