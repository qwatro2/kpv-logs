package backend.academy.logs.validators;

import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.ParsingResult;
import backend.academy.logs.entities.ValidationResult;
import java.util.Set;
import java.util.function.BiFunction;

public class LogsArgumentsValidator implements ArgumentsValidator {
    private BiFunction<ParsingResult, ValidationResult, Boolean> currentValidator;
    private final Validator localPathValidator;
    private final Validator urlPathValidator;
    private final Validator dateValidator;

    public LogsArgumentsValidator(Validator localPathValidator, Validator urlPathValidator, Validator dateValidator) {
        this.localPathValidator = localPathValidator;
        this.urlPathValidator = urlPathValidator;
        this.dateValidator = dateValidator;
    }

    @Override
    public ValidationResult validate(ParsingResult parsingResult) {
        ValidationResult validationResult = new ValidationResult().isValid(true).message("OK");

        currentValidator = this::validatePath;
        while (currentValidator.apply(parsingResult, validationResult));

        return validationResult;
    }

    private boolean validatePath(ParsingResult parsingResult, ValidationResult validationResult) {
        currentValidator = this::validateFrom;
        String path = parsingResult.path();

        if (path == null) {
            validationResult.isValid(false).message("Argument \"--path\" is required");
            return false;
        }

        if (!localPathValidator.validate(path) && !urlPathValidator.validate(path)) {
            validationResult.isValid(false).message("Argument \"--path\" should be local path or url");
            return false;
        }

        return true;
    }

    private boolean validateFrom(ParsingResult parsingResult, ValidationResult validationResult) {
        currentValidator = this::validateTo;

        if (parsingResult.from() == null) {
            return true;
        }

        if (!dateValidator.validate(parsingResult.from())) {
            validationResult.isValid(false).message("Argument \"--from\" should be in format YYYY-MM-DD");
            return false;
        }

        return true;
    }

    private boolean validateTo(ParsingResult parsingResult, ValidationResult validationResult) {
        currentValidator = this::validateFormat;

        if (parsingResult.to() == null) {
            return true;
        }

        if (!dateValidator.validate(parsingResult.to())) {
            validationResult.isValid(false).message("Argument \"--to\" should be in format YYYY-MM-DD");
            return false;
        }

        return true;
    }

    private boolean validateFormat(ParsingResult parsingResult, ValidationResult validationResult) {
        currentValidator = this::validateFilter;

        if (parsingResult.format() == null) {
            return true;
        }

        if (!parsingResult.format().equals("markdown") && !parsingResult.format().equals("adoc")) {
            validationResult.isValid(false).message("Argument \"--format\" should be \"markdown\" or \"adoc\"");
            return false;
        }

        return true;
    }

    private boolean validateFilter(ParsingResult parsingResult, ValidationResult validationResult) {
        currentValidator = (pr, vr) -> false;

        if ((parsingResult.filterField() == null) != (parsingResult.filterValue() == null)) {
            validationResult.isValid(false).message("Arguments \"--filter-field\" and " +
                "\"--filter-value\" should be used together");
            return false;
        }

        if (parsingResult.filterField() == null) {
            return true;
        }

        Set<String> fields = NginxLog.getSetOfField();

        if (!fields.contains(parsingResult.filterField())) {
            validationResult.isValid(false).message("Argument \"--filter-field\" should be " +
                "\"remote-address\", \"remote-user\", \"time-local\", \"request-type\", \"rout\", " +
                "\"http-version\", \"status\", \"body-bytes-send\", \"http-referer\" or \"http-user-agent\"");
            return false;
        }

        return true;
    }
}
