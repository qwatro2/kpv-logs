package backend.academy.logs.entities;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class ParsingResult {
    private String path;
    private String from;
    private String to;
    private String format;
    private String filterField;
    private String filterValue;
    private String output;

    public static boolean equals(ParsingResult lhs, ParsingResult rhs) {
        return Objects.equals(lhs.path, rhs.path)
            && Objects.equals(lhs.from, rhs.from)
            && Objects.equals(lhs.to, rhs.to)
            && Objects.equals(lhs.format, rhs.format)
            && Objects.equals(lhs.filterField, rhs.filterField)
            && Objects.equals(lhs.filterValue, rhs.filterValue)
            && Objects.equals(lhs.output, rhs.output);
    }

    public boolean equals(ParsingResult other) {
        return equals(this, other);
    }
}
