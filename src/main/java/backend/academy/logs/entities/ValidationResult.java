package backend.academy.logs.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResult {
    private boolean isValid;
    private String message;
}
