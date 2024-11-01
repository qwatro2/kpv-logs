package backend.academy.logs.parsers;

import backend.academy.logs.types.Months;
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
            case "Jan" -> Months.JANUARY;
            case "Feb" -> Months.FEBRUARY;
            case "Mar" -> Months.MARCH;
            case "Apr" -> Months.APRIL;
            case "May" -> Months.MAY;
            case "Jun" -> Months.JUNE;
            case "Jul" -> Months.JULY;
            case "Aug" -> Months.AUGUST;
            case "Sep" -> Months.SEPTEMBER;
            case "Oct" -> Months.OCTOBER;
            case "Nov" -> Months.NOVEMBER;
            case "Dec" -> Months.DECEMBER;
            default -> 0;
        };
    }
}
