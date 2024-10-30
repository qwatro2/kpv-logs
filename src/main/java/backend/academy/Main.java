package backend.academy;

import backend.academy.logs.app.App;
import backend.academy.logs.app.LogsAnalyzerApp;
import lombok.experimental.UtilityClass;

@UtilityClass public class Main {
    public static void main(String[] args) {
        App app = new LogsAnalyzerApp(System.err);
        app.run(args);
    }
}
