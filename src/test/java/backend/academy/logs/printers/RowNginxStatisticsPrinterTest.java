package backend.academy.logs.printers;

import backend.academy.logs.converters.RequestTypeConverter;
import backend.academy.logs.converters.StatusConverter;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RowNginxStatisticsPrinterTest {

    private ByteArrayOutputStream outputStream;
    private NginxStatisticsPrinter printer;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        printer = new RowNginxStatisticsPrinter(new StatusConverter(), new RequestTypeConverter());
        printer.setPrintStream(new PrintStream(outputStream));
    }

    @Test
    void testPrint() {
        List<String> fileNames = List.of("file1.log", "file2.log");
        LocalDate fromDate = LocalDate.of(2023, 10, 1);
        LocalDate toDate = LocalDate.of(2023, 10, 31);
        ParsingResult parsingResult = mock(ParsingResult.class);
        when(parsingResult.from()).thenReturn(fromDate.toString());
        when(parsingResult.to()).thenReturn(toDate.toString());

        HashMap<String, Integer> resources = new HashMap<>();
        resources.put("/index", 30);
        resources.put("/about", 15);

        HashMap<Integer, Integer> statuses = new HashMap<>();
        statuses.put(200, 50);
        statuses.put(404, 10);

        HashMap<RequestType, Integer> requestTypes = new HashMap<>();
        requestTypes.put(RequestType.GET, 40);
        requestTypes.put(RequestType.POST, 20);

        HashMap<LocalDate, Integer> dates = new HashMap<>();
        dates.put(LocalDate.of(2023, 10, 1), 10);
        dates.put(LocalDate.of(2023, 10, 2), 30);

        NginxStatistics statistics = new NginxStatistics()
            .names(fileNames)
            .numberOfRequests(100)
            .numberOfRequestsByResource(resources)
            .numberOfRequestsByStatus(statuses)
            .averageBodyBytesSent(500.0)
            .percentile(1000.0)
            .oldestLogTimestamp(LocalDateTime.of(2023, 10, 1, 10, 0))
            .newestLogTimestamp(LocalDateTime.of(2023, 10, 31, 22, 0))
            .numberOfRequestsByRequestType(requestTypes)
            .numberOfRequestsByDate(dates);

        printer.print(parsingResult, statistics);

        String expected = getExpected();
        String actual = outputStream.toString().trim().replaceAll("\r\n", "\n");

        assertEquals(expected, actual);
    }

    private static String getExpected() {
        String expectedOutput = """
                ~~~ Общая информация ~~~
                Файл(-ы): [file1.log, file2.log]
                Начальная дата: 2023-10-01
                Конечная дата: 2023-10-31
                Количество запросов: 100
                Средний размер ответа: 500.0
                95-перцентиль размера ответа: 1000.0
                Самая старая запись: 2023-10-01T10:00
                Самая новая запись: 2023-10-31T22:00
                ~~~ Ресурсы ~~~
                /index: 30
                /about: 15
                ~~~ Статусы ~~~
                OK: 50
                Not Found: 10
                ~~~ Методы ~~~
                GET: 40
                POST: 20
                ~~~ Даты ~~~
                2023-10-02: 30
                2023-10-01: 10
                """;

        return expectedOutput.trim().replaceAll("\r\n", "\n");
    }

    @Test
    void testRenderListAndRenderString() {
        List<String> files = List.of("file1.log", "file2.log");
        String renderedList = printer.renderList(files);
        assertEquals("[file1.log, file2.log]", renderedList);

        String renderedEmptyList = printer.renderList(List.of());
        assertEquals("-", renderedEmptyList);

        String renderedString = printer.renderString(null);
        assertEquals("-", renderedString);

        String nonNullString = printer.renderString("Sample text");
        assertEquals("Sample text", nonNullString);
    }
}
