package backend.academy;

import backend.academy.logs.file.FileGetter;
import backend.academy.logs.file.LocalFileGetter;
import backend.academy.logs.parsers.CommandLineParser;
import backend.academy.logs.parsers.LogsCommandLineParser;
import backend.academy.logs.entities.ParsingResult;
import lombok.experimental.UtilityClass;
import java.io.File;
import java.util.List;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        CommandLineParser clp = new LogsCommandLineParser();
        ParsingResult parsingResult = clp.parse(args);
        String path = "./src/main/java/backend/academy/**/*Validator.j*v*";
        FileGetter getter = new LocalFileGetter();
        List<File> files = getter.getFiles(path);
        for (File file : files) {
            print(file);
        }
    }

    private <T> void print(T object) {
        System.out.println(object);
    }
}
