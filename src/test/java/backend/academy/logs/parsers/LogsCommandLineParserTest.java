package backend.academy.logs.parsers;

import backend.academy.logs.entities.ParsingResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogsCommandLineParserTest {
    private final CommandLineParser clp = new LogsCommandLineParser();

    @Test
    void parseEmpty() {
        String[] args = new String[0];
        ParsingResult expected = new ParsingResult();
        ParsingResult actual = clp.parse(args);
        assertTrue(ParsingResult.equals(expected, actual));
    }

    @Test
    void parseNull() {
        ParsingResult expected = new ParsingResult();
        ParsingResult actual = clp.parse(null);
        assertTrue(ParsingResult.equals(expected, actual));
    }

    @Test
    void parseOneArgument() {
        String[] args = new String[1];
        args[0] = "--path";
        ParsingResult expected = new ParsingResult();
        ParsingResult actual = clp.parse(args);
        assertTrue(ParsingResult.equals(expected, actual));
    }

    @Test
    void parseOneAny() {
        String[] args = new String[1];
        args[0] = "aboba";
        ParsingResult expected = new ParsingResult();
        ParsingResult actual = clp.parse(args);
        assertTrue(ParsingResult.equals(expected, actual));
    }

    @Test
    void parseNormal() {
        String[] args = new String[4];
        args[0] = "--path";
        args[1] = "./a.test";
        args[2] = "--format";
        args[3] = "adoc";

        ParsingResult expected = new ParsingResult().path(args[1]).format(args[3]);
        ParsingResult actual = clp.parse(args);
        assertTrue(ParsingResult.equals(expected, actual));
    }
}
