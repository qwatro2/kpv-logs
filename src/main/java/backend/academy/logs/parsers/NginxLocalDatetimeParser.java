package backend.academy.logs.parsers;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxLocalDatetimeParser implements LocalDatetimeParser {
    @Override
    public LocalDateTime parse(String localDateTime) {
        String regex = "^(?<day>\\d{2})/(?<month>[A-Z][a-z]{2})/(?<year>\\d{4}):" +
            "(?<hour>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2}) (?<timezone>([+\\-])\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(localDateTime);

        if (!matcher.matches()) {
            return null;
        }

        try {
            return LocalDateTime.of(
                Integer.parseInt(matcher.group("year")),
                monthNumberFromString(matcher.group("month")),
                Integer.parseInt(matcher.group("day")),
                Integer.parseInt(matcher.group("hour")),
                Integer.parseInt(matcher.group("minute")),
                Integer.parseInt(matcher.group("second"))
            );
        } catch (DateTimeException e) {
            return null;
        }
    }

    private int monthNumberFromString(String month) {
        return switch (month) {
            case "Jan" -> 1;
            case "Feb" -> 2;
            case "Mar" -> 3;
            case "Apr" -> 4;
            case "May" -> 5;
            case "Jun" -> 6;
            case "Jul" -> 7;
            case "Aug" -> 8;
            case "Sep" -> 9;
            case "Oct" -> 10;
            case "Nov" -> 11;
            case "Dec" -> 12;
            default -> 0;
        };
    }
}
