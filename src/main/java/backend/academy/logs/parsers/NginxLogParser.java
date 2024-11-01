package backend.academy.logs.parsers;

import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.Request;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxLogParser implements LogParser<NginxLog> {
    private final RequestParser requestParser;
    private final LocalDatetimeParser datetimeParser;

    public NginxLogParser(RequestParser requestParser, LocalDatetimeParser datetimeParser) {
        this.requestParser = requestParser;
        this.datetimeParser = datetimeParser;
    }

    @Override
    public NginxLog parse(String log) {
        String regex = "^(?<remoteaddress>.*) -(?<remoteuser>.*)- "
                + "\\[(?<timelocal>.*)] "
            + "\"(?<request>.*)\" (?<status>\\d{3}) (?<bodybytessent>\\d*) "
            + "\"(?<httpreferer>.*)\" \"(?<httpuseragent>.*)\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(log);

        if (!matcher.matches()) {
            return null;
        }

        Request request = requestParser.parse(matcher.group("request"));
        if (request == null) {
            return null;
        }

        LocalDateTime localDateTime = datetimeParser.parse(matcher.group("timelocal"));
        if (localDateTime == null) {
            return null;
        }

        return new NginxLog(
            matcher.group("remoteaddress"),
            matcher.group("remoteuser"),
            localDateTime,
            request,
            Integer.parseInt(matcher.group("status")),
            Integer.parseInt(matcher.group("bodybytessent")),
            matcher.group("httpreferer"),
            matcher.group("httpuseragent")
        );
    }
}
