package backend.academy.logs.statistics;

import backend.academy.logs.types.RequestType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

@Getter
public class NginxStatistics extends AbstractStatistics {
    private final List<String> names;
    private final int numberOfRequests;
    private final HashMap<String, Integer> numberOfRequestsByResource;
    private final HashMap<Integer, Integer> numberOfRequestsByStatus;
    private final double averageBodyBytesSent;
    private final double percentile;
    private final LocalDateTime oldestLogTimestamp;
    private final LocalDateTime newestLogTimestamp;
    private final HashMap<RequestType, Integer> numberOfRequestsByRequestType;
    private final HashMap<LocalDate, Integer> numberOfRequestsByDate;

    public NginxStatistics(
        List<String> names, int numberOfRequests, HashMap<String, Integer> numberOfRequestsByResource,
        HashMap<Integer, Integer> numberOfRequestsByStatus,
        double averageBodyBytesSent,
        double percentile,
        LocalDateTime oldestLogTimestamp,
        LocalDateTime newestLogTimestamp, HashMap<RequestType, Integer> numberOfRequestsByRequestType,
        HashMap<LocalDate, Integer> numberOfRequestsByDate
    ) {
        this.names = names;
        this.numberOfRequests = numberOfRequests;
        this.numberOfRequestsByResource = numberOfRequestsByResource;
        this.numberOfRequestsByStatus = numberOfRequestsByStatus;
        this.averageBodyBytesSent = averageBodyBytesSent;
        this.percentile = percentile;
        this.oldestLogTimestamp = oldestLogTimestamp;
        this.newestLogTimestamp = newestLogTimestamp;
        this.numberOfRequestsByRequestType = numberOfRequestsByRequestType;
        this.numberOfRequestsByDate = numberOfRequestsByDate;
    }
}
