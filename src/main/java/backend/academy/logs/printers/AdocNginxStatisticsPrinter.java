package backend.academy.logs.printers;

import backend.academy.logs.converters.Converter;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdocNginxStatisticsPrinter extends NginxStatisticsPrinter {
    private static final String TABLE_TOP_BOTTOM = "|====";

    public AdocNginxStatisticsPrinter(Converter<Integer> statusConverter, Converter<RequestType> requestTypeConverter) {
        super(statusConverter, requestTypeConverter);
    }

    @Override
    protected void printBaseInformation(ParsingResult parsingResult, NginxStatistics statistics) {
        printStream.println("==== Общая информация");
        printStream.println(TABLE_TOP_BOTTOM);
        printStream.println("|Метрика |Значение");
        printStream.println("|Файл(-ы)\n|" + renderList(statistics.names()));
        printStream.println("|Начальная дата\n|" + renderString(parsingResult.from()));
        printStream.println("|Конечная дата\n|" + renderString(parsingResult.to()));
        printStream.println("|Количество запросов\n|" + statistics.numberOfRequests());
        printStream.println("|Средний размер ответа\n|" + statistics.averageBodyBytesSent());
        printStream.println("|95-перцентиль размера ответа\n|" + statistics.percentile());
        printStream.println("|Самая старая запись\n|" + statistics.oldestLogTimestamp());
        printStream.println("|Самая новая запись\n|" + statistics.newestLogTimestamp());
        printStream.println(TABLE_TOP_BOTTOM);
    }

    @Override
    protected <K> void printMapCounter(String title, HashMap<K, Integer> mapCounter, Function<K, String> keyRenderer) {
        printStream.println("==== " + title);
        printStream.println(TABLE_TOP_BOTTOM);
        printStream.println("|" + title + " |Количество");
        List<Map.Entry<K, Integer>> sortedKeyValues = getSortedMap(mapCounter);
        for (Map.Entry<K, Integer> keyValue : sortedKeyValues) {
            printStream.println("|" + keyRenderer.apply(keyValue.getKey()) + "\n|" + keyValue.getValue());
        }
        printStream.println(TABLE_TOP_BOTTOM);
    }
}
