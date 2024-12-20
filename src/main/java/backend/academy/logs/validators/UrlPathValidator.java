package backend.academy.logs.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlPathValidator implements Validator {
    @Override
    public boolean validate(String value) {
        String regex = "^(https?|ftp)://([a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+)(:\\d+)?(/.*)?(\\?.*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
