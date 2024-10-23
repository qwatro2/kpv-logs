package backend.academy.logs.parsers;

import backend.academy.logs.entities.ParsingResult;
import java.util.function.Function;

public class LogsCommandLineParser implements CommandLineParser {

    @Override
    public ParsingResult parse(String[] args) {
        ParsingResult parsingResult = new ParsingResult();

        if (args == null) {
            return parsingResult;
        }

        for (int i = 0; i < args.length - 1; ++i) {
            Function<String, ParsingResult> modify = switch (args[i]) {
                case "--path" -> parsingResult::path;
                case "--from" -> parsingResult::from;
                case "--to" -> parsingResult::to;
                case "--format" -> parsingResult::format;
                case "--filter-field" -> parsingResult::filterField;
                case "--filter-value" -> parsingResult::filterValue;
                default -> null;
            };

            if (modify != null) {
                modify.apply(args[i + 1]);
            }
        }

        return parsingResult;
    }
}
