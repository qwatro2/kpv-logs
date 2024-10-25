package backend.academy.logs.files;

import backend.academy.logs.entities.LogStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalLogStreamGetterTest {

    private LocalLogStreamGetter logStreamGetter;
    private Path tempDir;

    @BeforeEach
    public void setUp() throws IOException {
        logStreamGetter = new LocalLogStreamGetter();
        tempDir = Files.createTempDirectory("testDir");
    }

    @Test
    public void testSingleFile() throws IOException {
        Path file = Files.createFile(tempDir.resolve("test1.log"));
        Files.writeString(file, "Line 1\nLine 2");

        LogStream logStream = logStreamGetter.getStream(file.toString());

        assertEquals(List.of(file.toString()), logStream.names());
        List<String> lines = logStream.stream().collect(Collectors.toList());
        assertEquals(List.of("Line 1", "Line 2"), lines);
    }

    @Test
    public void testMultipleFiles() throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("test1.log"));
        Path file2 = Files.createFile(tempDir.resolve("test2.log"));
        Files.writeString(file1, "File1 Line 1\nFile1 Line 2");
        Files.writeString(file2, "File2 Line 1\nFile2 Line 2");

        LogStream logStream = logStreamGetter.getStream(tempDir + "/*.log");

        assertEquals(List.of(file1.toString(), file2.toString()), logStream.names());
        List<String> lines = logStream.stream().collect(Collectors.toList());
        assertEquals(List.of("File1 Line 1", "File1 Line 2", "File2 Line 1", "File2 Line 2"), lines);
    }

    @Test
    public void testEmptyFile() throws IOException {
        Path file = Files.createFile(tempDir.resolve("empty.log"));

        LogStream logStream = logStreamGetter.getStream(file.toString());

        assertEquals(List.of(file.toString()), logStream.names());
        List<String> lines = logStream.stream().toList();
        assertTrue(lines.isEmpty());
    }

    @Test
    public void testFileNotFound() {
        LogStream logStream = logStreamGetter.getStream(tempDir + "/nonexistent.log");

        assertTrue(logStream.stream().findAny().isEmpty());
    }

    @Test
    public void testMixedFileTypes() throws IOException {
        Path logFile = Files.createFile(tempDir.resolve("test.log"));
        Path textFile = Files.createFile(tempDir.resolve("test.txt"));
        Files.writeString(logFile, "Log file line");
        Files.writeString(textFile, "Text file line");

        LogStream logStream = logStreamGetter.getStream(tempDir + "/*.log");

        assertEquals(List.of(logFile.toString()), logStream.names());
        List<String> lines = logStream.stream().collect(Collectors.toList());
        assertEquals(List.of("Log file line"), lines);
    }

    @Test
    public void testGlobPattern() throws IOException {
        Files.createDirectories(tempDir.resolve("dir1"));
        Files.createDirectories(tempDir.resolve("dir2"));
        Path file1 = Files.createFile(tempDir.resolve("dir1/test1.log"));
        Path file2 = Files.createFile(tempDir.resolve("dir2/test2.log"));
        Files.writeString(file1, "Dir1 Log Line");
        Files.writeString(file2, "Dir2 Log Line");

        LogStream logStream = logStreamGetter.getStream(tempDir + "/**/*.log");

        assertEquals(List.of(file1.toString(), file2.toString()), logStream.names());
        List<String> lines = logStream.stream().collect(Collectors.toList());
        assertEquals(List.of("Dir1 Log Line", "Dir2 Log Line"), lines);
    }

    @AfterEach
    public void setDown() throws IOException {
        try (Stream<Path> pathStream = Files.walk(tempDir)) {
            pathStream.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}
