package backend.academy.logs.filters;

import backend.academy.logs.entities.NginxLog;
import backend.academy.logs.entities.ParsingResult;
import java.util.function.Predicate;

public interface FilterBuilder {
    Predicate<NginxLog> build(ParsingResult parsingResult);
}
