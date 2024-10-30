package backend.academy.logs.collectors;

import backend.academy.logs.entities.LogStream;
import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.Request;
import backend.academy.logs.parsers.LogParser;
import backend.academy.logs.percentiles.PercentileCounter;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class NginxStatisticsCollectorTest {

    private LogParser<NginxLog> logParser;
    private PercentileCounter percentileCounter;
    private NginxStatisticsCollector collector;

    @BeforeEach
    void setUp() {
        logParser = mock(LogParser.class);
        percentileCounter = mock(PercentileCounter.class);
        collector = new NginxStatisticsCollector(logParser, percentileCounter);
    }

    @Test
    void testCollectStatisticsEmptyLogStream() {
        LogStream emptyLogStream = new LogStream(List.of(), Stream.of());

        NginxStatistics stats = collector.collectStatistics(emptyLogStream);

        assertEquals(0, stats.numberOfRequests());
        assertEquals(0, stats.numberOfRequestsByResource().size());
        assertEquals(0, stats.numberOfRequestsByStatus().size());
        assertEquals(0.0, stats.averageBodyBytesSent());
        assertEquals(0.0, stats.percentile());
        assertNull(stats.oldestLogTimestamp());
        assertNull(stats.newestLogTimestamp());
    }

    @Test
    void testCollectStatisticsSingleLog() {
        LocalDateTime timestamp = LocalDateTime.now();
        NginxLog log = new NginxLog("192.168.0.1", "user1", timestamp,
            new Request(RequestType.GET, "/resource", "HTTP/1.1"), 200, 512, "http://referer", "agent");

        when(logParser.parse(anyString())).thenReturn(log);
        LogStream singleLogStream = new LogStream(List.of("log1"), Stream.of("some log line"));

        NginxStatistics stats = collector.collectStatistics(singleLogStream);

        assertEquals(1, stats.numberOfRequests());
        assertEquals(1, stats.numberOfRequestsByResource().get("/resource"));
        assertEquals(1, stats.numberOfRequestsByStatus().get(200));
        assertEquals(512.0, stats.averageBodyBytesSent());
        assertEquals(timestamp, stats.oldestLogTimestamp());
        assertEquals(timestamp, stats.newestLogTimestamp());
        verify(percentileCounter, times(1)).processNext(512);
        verify(percentileCounter, times(1)).getPercentile();
    }

    @Test
    void testCollectStatisticsMultipleLogs() {
        LocalDateTime time1 = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 10, 2, 14, 0);
        NginxLog log1 = new NginxLog("192.168.0.1", "user1", time1,
            new Request(RequestType.GET, "/resource1", "HTTP/1.1"), 200, 400, "http://referer", "agent1");
        NginxLog log2 = new NginxLog("192.168.0.2", "user2", time2,
            new Request(RequestType.POST, "/resource2", "HTTP/1.1"), 404, 600, "http://referer", "agent2");

        when(logParser.parse("log line 1")).thenReturn(log1);
        when(logParser.parse("log line 2")).thenReturn(log2);

        LogStream multiLogStream = new LogStream(List.of("log1", "log2"), Stream.of("log line 1", "log line 2"));
        NginxStatistics stats = collector.collectStatistics(multiLogStream);

        assertEquals(2, stats.numberOfRequests());
        assertEquals(1, stats.numberOfRequestsByResource().get("/resource1"));
        assertEquals(1, stats.numberOfRequestsByResource().get("/resource2"));
        assertEquals(1, stats.numberOfRequestsByStatus().get(200));
        assertEquals(1, stats.numberOfRequestsByStatus().get(404));
        assertEquals((400 + 600) / 2.0, stats.averageBodyBytesSent());
        assertEquals(time1, stats.oldestLogTimestamp());
        assertEquals(time2, stats.newestLogTimestamp());
        verify(percentileCounter, times(1)).processNext(400);
        verify(percentileCounter, times(1)).processNext(600);
        verify(percentileCounter, times(1)).getPercentile();
    }

    @Test
    void testPercentileCalculation() {
        LocalDateTime time = LocalDateTime.now();
        NginxLog log1 = new NginxLog("192.168.0.1", "user1", time,
            new Request(RequestType.GET, "/resource", "HTTP/1.1"), 200, 500, "http://referer", "agent");

        when(logParser.parse(anyString())).thenReturn(log1);
        when(percentileCounter.getPercentile()).thenReturn(500.0);
        LogStream logStream = new LogStream(List.of("log1"), Stream.of("log line"));

        NginxStatistics stats = collector.collectStatistics(logStream);

        assertEquals(500.0, stats.percentile());
    }
}
