package backend.academy.logs.printers;

import backend.academy.logs.converters.Converter;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class NginxStatisticsPrinter implements StatisticsPrinter<NginxStatistics> {
    protected PrintStream printStream;

    protected final static int MAX_MAP_ROWS_COUNT = 5;
    private final Converter<Integer> statusConverter;
    private final Converter<RequestType> requestTypeConverter;

    public NginxStatisticsPrinter(Converter<Integer> statusConverter, Converter<RequestType> requestTypeConverter) {
        this.statusConverter = statusConverter;
        this.requestTypeConverter = requestTypeConverter;
    }

    @Override
    public final void print(ParsingResult parsingResult, NginxStatistics statistics) {
        printBaseInformation(parsingResult, statistics);
        printMapCounter("Ресурсы", statistics.numberOfRequestsByResource(), (s) -> s);
        printMapCounter("Статусы", statistics.numberOfRequestsByStatus(), statusConverter::convert);
        printMapCounter("Методы", statistics.numberOfRequestsByRequestType(), requestTypeConverter::convert);
        printMapCounter("Даты", statistics.numberOfRequestsByDate(), LocalDate::toString);
    }

    protected abstract void printBaseInformation(ParsingResult parsingResult, NginxStatistics statistics);

    protected abstract <K> void printMapCounter(
        String title,
        HashMap<K, Integer> mapCounter,
        Function<K, String> keyRenderer
    );

    @Override
    public StatisticsPrinter<NginxStatistics> setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
        return this;
    }

    protected <T> String renderList(List<T> list) {
        return switch (list.size()) {
            case 0 -> "-";
            case 1 -> list.getFirst().toString();
            default -> list.toString();
        };
    }

    protected String renderString(String string) {
        if (string == null) {
            return "-";
        }
        return string;
    }

    protected <K> List<Map.Entry<K, Integer>> getSortedMap(HashMap<K, Integer> map) {
        return map.entrySet().stream()
            .sorted((lhs, rhs) -> rhs.getValue() - lhs.getValue())
            .limit(Math.min(MAX_MAP_ROWS_COUNT, map.size())).toList();
    }
}
