package backend.academy.logs.validators;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator implements Validator {
    @Override
    public boolean validate(String value) {
        String regex = "^(\\d{4})-(\\d{2})-(\\d{2})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches() && validateNumbers(value);
    }

    private boolean validateNumbers(String value) {
        List<Integer> numbers = Arrays.stream(value.split("-")).map(Integer::parseInt).toList();
        try {
            LocalDate.of(numbers.getFirst(), numbers.get(1), numbers.get(2));
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
