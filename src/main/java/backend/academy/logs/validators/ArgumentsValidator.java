package backend.academy.logs.validators;

import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.entities.ValidationResult;

public interface ArgumentsValidator {
    ValidationResult validate(ParsingResult parsingResult);
}
