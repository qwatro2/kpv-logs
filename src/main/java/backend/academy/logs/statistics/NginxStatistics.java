package backend.academy.logs.statistics;

import backend.academy.logs.types.RequestType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NginxStatistics extends AbstractStatistics {
    private List<String> names;
    private int numberOfRequests;
    private HashMap<String, Integer> numberOfRequestsByResource;
    private HashMap<Integer, Integer> numberOfRequestsByStatus;
    private double averageBodyBytesSent;
    private double percentile;
    private LocalDateTime oldestLogTimestamp;
    private LocalDateTime newestLogTimestamp;
    private HashMap<RequestType, Integer> numberOfRequestsByRequestType;
    private HashMap<LocalDate, Integer> numberOfRequestsByDate;
}
