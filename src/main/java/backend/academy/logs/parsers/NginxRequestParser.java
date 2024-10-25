package backend.academy.logs.parsers;

import backend.academy.logs.entities.Request;
import backend.academy.logs.types.RequestType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxRequestParser implements RequestParser {
    @Override
    public Request parse(String request) {
        String regex = "^(?<method>[A-Z]*) (?<rout>(/[a-zA-Z0-9_]+)+) (?<httpversion>HTTP/([0-9.]+|\\d))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(request);

        if (!matcher.matches()) {
            return null;
        }

        RequestType requestType;
        try {
            requestType = RequestType.valueOf(matcher.group("method"));
        } catch (IllegalArgumentException exception) {
            return null;
        }

        return new Request(
            requestType,
            matcher.group("rout"),
            matcher.group("httpversion")
        );
    }
}
