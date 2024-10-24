package backend.academy.logs.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalPathValidator implements PathValidator {
    @Override
    public boolean validate(String path) {
        String regex = "^(?!.*//)([a-zA-Z]:)?" +
            "([./a-zA-Z0-9_-]+(/([a-zA-Z0-9_.-]+|\\*\\*|\\*))*" +
            "(((/((?!.*\\*\\*)[a-zA-Z0-9_.\\-*]+))\\.([a-zA-Z0-9*]+))|([a-zA-Z0-9_-]+\\*)))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }
}
