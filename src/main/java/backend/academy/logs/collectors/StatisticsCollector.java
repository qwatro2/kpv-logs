package backend.academy.logs.collectors;

import backend.academy.logs.entities.LogStream;
import backend.academy.logs.statistics.AbstractStatistics;

public interface StatisticsCollector<T extends AbstractStatistics> {
    T collectStatistics(LogStream logStream);
}
