package backend.academy.logs.parsers;

import backend.academy.logs.types.Month;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxLocalDatetimeParser implements LocalDatetimeParser {
    @Override
    public LocalDateTime parse(String localDateTime) {
        String regex = "^(?<day>\\d{2})/(?<month>[A-Z][a-z]{2})/(?<year>\\d{4}):"
            + "(?<hour>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2}) (?<timezone>([+\\-])\\d{4})$";
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
            case "Jan" -> Month.JANUARY;
            case "Feb" -> Month.FEBRUARY;
            case "Mar" -> Month.MARCH;
            case "Apr" -> Month.APRIL;
            case "May" -> Month.MAY;
            case "Jun" -> Month.JUNE;
            case "Jul" -> Month.JULY;
            case "Aug" -> Month.AUGUST;
            case "Sep" -> Month.SEPTEMBER;
            case "Oct" -> Month.OCTOBER;
            case "Nov" -> Month.NOVEMBER;
            case "Dec" -> Month.DECEMBER;
            default -> 0;
        };
    }
}
