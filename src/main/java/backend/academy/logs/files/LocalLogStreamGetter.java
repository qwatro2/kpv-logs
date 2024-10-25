package backend.academy.logs.files;

import backend.academy.logs.entities.LogStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class LocalLogStreamGetter implements LogStreamGetter {
    private final LocalPathGetter localPathGetter = new LocalPathGetter();

    @Override
    public LogStream getStream(String path) {
        List<Path> paths = localPathGetter.getPaths(path);
        Stream<String> resultingStream = Stream.of();

        for (Path p : paths) {
            Stream<String> currentStream;
            try {
                currentStream = Files.lines(p);
            } catch (IOException e) {
                continue;
            }
            resultingStream = Stream.concat(resultingStream, currentStream);
        }

        return new LogStream(paths.stream().map(Path::toString).toList(), resultingStream);
    }
}
