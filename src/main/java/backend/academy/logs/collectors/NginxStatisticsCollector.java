package backend.academy.logs.collectors;

import backend.academy.logs.entities.LogStream;
import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.parsers.LogParser;
import backend.academy.logs.percentiles.PercentileCounter;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.function.Predicate;

public class NginxStatisticsCollector implements StatisticsCollector<NginxStatistics> {
    private final static double TARGET_PERCENTILE = 0.95;

    private final LogParser<NginxLog> logParser;
    private final PercentileCounter percentileCounter;
    private final Predicate<NginxLog> filterPredicate;

    private int numberOfRequests = 0;
    private HashMap<String, Integer> numberOfRequestsByResource = new HashMap<>();
    private HashMap<Integer, Integer> numberOfRequestsByStatus = new HashMap<>();
    private long sumBodyBytesSent = 0;
    private LocalDateTime oldestLogTimestamp = null;
    private LocalDateTime newestLogTimestamp = null;
    private HashMap<RequestType, Integer> numberOfRequestsByRequestType = new HashMap<>();
    private HashMap<LocalDate, Integer> numberOfRequestsByDate = new HashMap<>();

    public NginxStatisticsCollector(LogParser<NginxLog> logParser, PercentileCounter percentileCounter,
        Predicate<NginxLog> filterPredicate
    ) {
        this.logParser = logParser;
        this.percentileCounter = percentileCounter;
        this.filterPredicate = filterPredicate;
    }

    @Override
    public NginxStatistics collectStatistics(LogStream logStream) {
        reset();
        logStream.stream().map(logParser::parse).filter(filterPredicate).forEach(this::process);
        return new NginxStatistics(logStream.names(), numberOfRequests, numberOfRequestsByResource,
            numberOfRequestsByStatus, averageBodyBytesSent(), percentileCounter.getPercentile(),
            oldestLogTimestamp, newestLogTimestamp,
            numberOfRequestsByRequestType, numberOfRequestsByDate);
    }

    private double averageBodyBytesSent() {
        return numberOfRequests > 0 ? (double) sumBodyBytesSent / numberOfRequests : 0.0;
    }

    private void reset() {
        numberOfRequests = 0;
        numberOfRequestsByResource = new HashMap<>();
        numberOfRequestsByStatus = new HashMap<>();
        sumBodyBytesSent = 0;
        percentileCounter.reset(TARGET_PERCENTILE);
        oldestLogTimestamp = null;
        newestLogTimestamp = null;
        numberOfRequestsByRequestType = new HashMap<>();
        numberOfRequestsByDate = new HashMap<>();
    }

    private void process(NginxLog log) {
        ++numberOfRequests;
        numberOfRequestsByResource.merge(log.request().rout(), 1, Integer::sum);
        numberOfRequestsByStatus.merge(log.status(), 1, Integer::sum);
        sumBodyBytesSent += log.bodyBytesSent();
        percentileCounter.processNext(log.bodyBytesSent());
        if (oldestLogTimestamp == null || oldestLogTimestamp.isAfter(log.timeLocal())) {
            oldestLogTimestamp = log.timeLocal();
        }
        if (newestLogTimestamp == null || newestLogTimestamp.isBefore(log.timeLocal())) {
            newestLogTimestamp = log.timeLocal();
        }
        numberOfRequestsByRequestType.merge(log.request().requestType(), 1, Integer::sum);
        numberOfRequestsByDate.merge(log.timeLocal().toLocalDate(), 1, Integer::sum);
    }
}
