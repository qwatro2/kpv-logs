package backend.academy.logs.parsers;

import backend.academy.logs.entities.ParsingResult;

public interface CommandLineParser {
    ParsingResult parse(String[] args);
}
