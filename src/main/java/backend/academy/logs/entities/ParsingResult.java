package backend.academy.logs.entities;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class ParsingResult {
    private boolean help;
    private String path;
    private String from;
    private String to;
    private String format;
    private String filterField;
    private String filterValue;

    public static boolean equals(ParsingResult lhs, ParsingResult rhs) {
        return lhs.help == rhs.help
            && Objects.equals(lhs.path, rhs.path)
            && Objects.equals(lhs.from, rhs.from)
            && Objects.equals(lhs.to, rhs.to)
            && Objects.equals(lhs.format, rhs.format)
            && Objects.equals(lhs.filterField, rhs.filterField)
            && Objects.equals(lhs.filterValue, rhs.filterValue);
    }

    public boolean equals(ParsingResult other) {
        return equals(this, other);
    }
}
