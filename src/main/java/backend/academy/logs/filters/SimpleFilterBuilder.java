package backend.academy.logs.filters;

import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.ParsingResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleFilterBuilder implements FilterBuilder {
    @Override
    public Predicate<NginxLog> build(ParsingResult parsingResult) {
        return processFrom(parsingResult)
            .and(processTo(parsingResult))
            .and(processFilterField(parsingResult));
    }

    private Predicate<NginxLog> processFrom(ParsingResult parsingResult) {
        if (parsingResult.from() == null) {
            return (log) -> true;
        }
        LocalDate fromDate = LocalDate.parse(parsingResult.from());
        return (log) -> log.timeLocal().toLocalDate().isEqual(fromDate)
            || log.timeLocal().toLocalDate().isAfter(fromDate);
    }

    private Predicate<NginxLog> processTo(ParsingResult parsingResult) {
        if (parsingResult.to() == null) {
            return (log) -> true;
        }
        LocalDate toDate = LocalDate.parse(parsingResult.to());
        return (log) -> log.timeLocal().toLocalDate().isEqual(toDate)
            || log.timeLocal().toLocalDate().isBefore(toDate);
    }

    private Predicate<NginxLog> processFilterField(ParsingResult parsingResult) {
        return switch (parsingResult.filterField()) {
            case null -> (log) -> true;
            case "time-local" -> processFilterFieldData(parsingResult);
            case "status" -> processFilterFieldInteger(parsingResult, NginxLog::status);
            case "body-bytes-sent" -> processFilterFieldInteger(parsingResult, NginxLog::bodyBytesSent);
            default -> processFilterFieldString(parsingResult);
        };
    }

    private Predicate<NginxLog> processFilterFieldData(ParsingResult parsingResult) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(parsingResult.filterValue());
            return (log) -> localDateTime.isEqual(log.timeLocal());
        } catch (DateTimeParseException e) {
            return (log) -> true;
        }
    }

    private Predicate<NginxLog> processFilterFieldInteger(
        ParsingResult parsingResult,
        Function<NginxLog, Integer> valueGetter
    ) {
        try {
            int value = Integer.parseInt(parsingResult.filterValue());
            return (log) -> value == valueGetter.apply(log);
        } catch (NumberFormatException e) {
            return (log) -> true;
        }
    }

    private Predicate<NginxLog> processFilterFieldString(ParsingResult parsingResult) {
        Function<NginxLog, String> actualField = switch (parsingResult.filterField()) {
            case "remote-address" -> NginxLog::remoteAddress;
            case "remote-user" -> NginxLog::remoteUser;
            case "request-type" -> (log) -> log.request().requestType().toString();
            case "rout" -> (log) -> log.request().rout();
            case "http-version" -> (log) -> log.request().httpVersion();
            case "http-referer" -> NginxLog::httpReferer;
            case "http-user-agent" -> NginxLog::httpUserAgent;
            default -> (log) -> parsingResult.filterValue();
        };
        return (log) -> Objects.equals(actualField.apply(log), parsingResult.filterValue());
    }
}
