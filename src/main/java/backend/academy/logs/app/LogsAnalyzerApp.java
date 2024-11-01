package backend.academy.logs.app;

import backend.academy.logs.collectors.NginxStatisticsCollector;
import backend.academy.logs.collectors.StatisticsCollector;
import backend.academy.logs.converters.Converter;
import backend.academy.logs.converters.RequestTypeConverter;
import backend.academy.logs.converters.StatusConverter;
import backend.academy.logs.entities.LogStream;
import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.entities.ValidationResult;
import backend.academy.logs.files.LocalLogStreamGetter;
import backend.academy.logs.files.LocalPathGetter;
import backend.academy.logs.files.LogStreamGetter;
import backend.academy.logs.files.UrlLogStreamGetter;
import backend.academy.logs.filters.FilterBuilder;
import backend.academy.logs.filters.SimpleFilterBuilder;
import backend.academy.logs.parsers.CommandLineParser;
import backend.academy.logs.parsers.LocalDatetimeParser;
import backend.academy.logs.parsers.LogParser;
import backend.academy.logs.parsers.LogsCommandLineParser;
import backend.academy.logs.parsers.NginxLocalDatetimeParser;
import backend.academy.logs.parsers.NginxLogParser;
import backend.academy.logs.parsers.NginxRequestParser;
import backend.academy.logs.parsers.RequestParser;
import backend.academy.logs.percentiles.PercentileCounter;
import backend.academy.logs.percentiles.TDigestPercentileCounter;
import backend.academy.logs.printers.AdocNginxStatisticsPrinter;
import backend.academy.logs.printers.MarkdownNginxStatisticsPrinter;
import backend.academy.logs.printers.NginxStatisticsPrinter;
import backend.academy.logs.printers.RowNginxStatisticsPrinter;
import backend.academy.logs.printers.StatisticsPrinter;
import backend.academy.logs.statistics.NginxStatistics;
import backend.academy.logs.types.RequestType;
import backend.academy.logs.validators.ArgumentsValidator;
import backend.academy.logs.validators.DateValidator;
import backend.academy.logs.validators.LocalPathValidator;
import backend.academy.logs.validators.LogsArgumentsValidator;
import backend.academy.logs.validators.UrlPathValidator;
import backend.academy.logs.validators.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import org.openjdk.jmh.util.FileUtils;

public class LogsAnalyzerApp implements App {
    private final PrintStream messagePrintStream;

    private final Validator localPathValidator;
    private final Validator urlPathValidator;

    public LogsAnalyzerApp(PrintStream messagePrintStream) {
        this.messagePrintStream = messagePrintStream;

        this.localPathValidator = new LocalPathValidator();
        this.urlPathValidator = new UrlPathValidator();
    }

    @Override
    public void run(String[] args) {
        ParsingResult parsingResult = getCommandLineArguments(args);

        ValidationResult validationResult = getValidationResult(parsingResult);
        if (!validationResult.isValid()) {
            messagePrintStream.println(validationResult.message());
            return;
        }

        LogStream logStream = getLogStreamGetter(parsingResult).getStream(parsingResult.path());
        Predicate<NginxLog> filterPredicate = getFilterPredicate(parsingResult);
        NginxStatistics statistics = getStatisticsCollector(filterPredicate).collectStatistics(logStream);
        StatisticsPrinter<NginxStatistics> statisticsPrinter = getStatisticsPrinter(parsingResult);
        statisticsPrinter.print(parsingResult, statistics);
    }

    private ParsingResult getCommandLineArguments(String[] args) {
        CommandLineParser commandLineParser = new LogsCommandLineParser();
        return commandLineParser.parse(args);
    }

    private ValidationResult getValidationResult(ParsingResult parsingResult) {
        Validator dateValidator = new DateValidator();
        ArgumentsValidator argumentsValidator =
            new LogsArgumentsValidator(localPathValidator, urlPathValidator, dateValidator);
        return argumentsValidator.validate(parsingResult);
    }

    private LogStreamGetter getLogStreamGetter(ParsingResult parsingResult) {
        if (localPathValidator.validate(parsingResult.path())) {
            return new LocalLogStreamGetter(new LocalPathGetter());
        }

        return new UrlLogStreamGetter();
    }

    private Predicate<NginxLog> getFilterPredicate(ParsingResult parsingResult) {
        FilterBuilder filterBuilder = new SimpleFilterBuilder();
        return filterBuilder.build(parsingResult);
    }

    private StatisticsCollector<NginxStatistics> getStatisticsCollector(Predicate<NginxLog> filterPredicate) {
        RequestParser requestParser = new NginxRequestParser();
        LocalDatetimeParser localDatetimeParser = new NginxLocalDatetimeParser();
        LogParser<NginxLog> logParser = new NginxLogParser(requestParser, localDatetimeParser);
        PercentileCounter percentileCounter = new TDigestPercentileCounter();
        return new NginxStatisticsCollector(logParser, percentileCounter, filterPredicate);
    }

    private PrintStream getStatisticsPrintStream(ParsingResult parsingResult) {
        if (parsingResult.output() == null) {
            return System.out;
        }
        try {
            FileUtils.writeLines(new File(parsingResult.output()), List.of());
            return new PrintStream(new FileOutputStream(parsingResult.output(), true));
        } catch (FileNotFoundException e) {
            messagePrintStream.println("File " + parsingResult.output()
                + " cannot be created or cannot be opened. Output redirected to System.out");
            return System.out;
        } catch (IOException e) {
            messagePrintStream.println("Error due clear file. Output redirected to System.out");
            return System.out;
        }
    }

    private StatisticsPrinter<NginxStatistics> getStatisticsPrinter(ParsingResult parsingResult) {
        BiFunction<Converter<Integer>, Converter<RequestType>, NginxStatisticsPrinter> printerGenerator =
            switch (parsingResult.format()) {
                case "markdown" -> MarkdownNginxStatisticsPrinter::new;
                case "adoc" -> AdocNginxStatisticsPrinter::new;
                case null, default -> RowNginxStatisticsPrinter::new;
            };
        Converter<Integer> statusConverter = new StatusConverter();
        Converter<RequestType> requestTypeConverter = new RequestTypeConverter();

        PrintStream statisticsPrintStream = getStatisticsPrintStream(parsingResult);
        return printerGenerator.apply(statusConverter, requestTypeConverter)
            .setPrintStream(statisticsPrintStream);
    }
}
