package backend.academy.logs.commandlineparser;

import backend.academy.logs.entities.ParsingResult;

public interface CommandLineParser {
    ParsingResult parse(String[] args);
}
