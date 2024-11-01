package backend.academy.logs.printers;

import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.statistics.AbstractStatistics;
import java.io.PrintStream;

public interface StatisticsPrinter<T extends AbstractStatistics> {
    StatisticsPrinter<T> setPrintStream(PrintStream printStream);

    void print(ParsingResult parsingResult, T statistics);
}
