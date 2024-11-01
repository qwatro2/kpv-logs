package backend.academy.logs.printers;

import backend.academy.logs.converters.Converter;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MarkdownNginxStatisticsPrinter extends NginxStatisticsPrinter {
    private static final String TABLE_SEPARATOR = "|:-:|-:|";

    public MarkdownNginxStatisticsPrinter(
        Converter<Integer> statusConverter,
        Converter<RequestType> requestTypeConverter
    ) {
        super(statusConverter, requestTypeConverter);
    }

    @Override
    protected void printBaseInformation(ParsingResult parsingResult, NginxStatistics statistics) {
        printStream.println("#### Общая информация");
        printStream.println("|Метрика|Значение|");
        printStream.println(TABLE_SEPARATOR);
        printStream.println("|Файл(-ы)|" + renderList(statistics.names()) + "|");
        printStream.println("|Начальная дата|" + renderString(parsingResult.from()) + "|");
        printStream.println("|Конечная дата|" + renderString(parsingResult.to()) + "|");
        printStream.println("|Количество запросов|" + statistics.numberOfRequests() + "|");
        printStream.println("|Средний размер ответа|" + statistics.averageBodyBytesSent() + "|");
        printStream.println("|95-перцентиль размера ответа|" + statistics.percentile() + "|");
        printStream.println("|Самая старая запись|" + statistics.oldestLogTimestamp() + "|");
        printStream.println("|Самая новая запись|" + statistics.newestLogTimestamp() + "|");
    }

    @Override
    protected <K> void printMapCounter(String title, HashMap<K, Integer> mapCounter, Function<K, String> keyRenderer) {
        printStream.println("#### " + title);
        printStream.println("|" + title + "|Количество|");
        printStream.println(TABLE_SEPARATOR);
        List<Map.Entry<K, Integer>> sortedKeyValues = getSortedMap(mapCounter);
        for (Map.Entry<K, Integer> keyValue : sortedKeyValues) {
            printStream.println("|" + keyRenderer.apply(keyValue.getKey()) + "|" + keyValue.getValue() + "|");
        }
    }
}
