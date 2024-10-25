package backend.academy.logs.files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class LocalPathGetterTest {

    private static LocalPathGetter pathGetter;
    private static Path prevTestDir;
    private static Path testDir;
    private static String glob;

    @BeforeAll
    public static void setUp() throws IOException {
        pathGetter = new LocalPathGetter();

        // Создаем временную тестовую директорию с файлами для проверки
        prevTestDir = Files.createTempDirectory("prevTestDir");
        glob = prevTestDir.toString();
        testDir = prevTestDir.resolve("testDir");
        Files.createDirectories(testDir);
        Files.createDirectories(testDir.resolve("dir/subdir"));
        Files.createFile(testDir.resolve("file1.txt"));
        Files.createFile(testDir.resolve("file2.log"));
        Files.createFile(testDir.resolve("dir/file3.txt"));
        Files.createFile(testDir.resolve("dir/subdir/file4.java"));
    }

    @Test
    public void testSingleFilePath() {
        List<Path> paths = pathGetter.getPaths(testDir.resolve("file1.txt").toString());
        assertEquals(1, paths.size());
        assertTrue(paths.contains(testDir.resolve("file1.txt")));
    }

    @Test
    public void testSingleDirectoryPath() {
        List<Path> paths = pathGetter.getPaths(testDir.resolve("dir").toString());
        assertEquals(1, paths.size());
        assertTrue(paths.contains(testDir.resolve("dir")));
    }

    @Test
    public void testSimpleWildcard() {
        List<Path> paths = pathGetter.getPaths(glob + "/testDir/*.txt");
        assertEquals(1, paths.size());
        assertTrue(paths.contains(testDir.resolve("file1.txt")));
    }

    @Test
    public void testWildcardInSubdirectory() {
        List<Path> paths = pathGetter.getPaths(glob + "/testDir/dir/*.txt");
        assertEquals(1, paths.size());
        assertTrue(paths.contains(testDir.resolve("dir/file3.txt")));
    }

    @Test
    public void testDoubleAsteriskWildcard() {
        List<Path> paths = pathGetter.getPaths(prevTestDir + "/**/*.txt");
        assertEquals(2, paths.size());
        assertTrue(paths.contains(testDir.resolve("file1.txt")));
        assertTrue(paths.contains(testDir.resolve("dir/file3.txt")));
    }

    @Test
    public void testMultipleExtensions() {
        List<Path> paths = pathGetter.getPaths(testDir + "**/*.java");
        assertEquals(1, paths.size());
        assertTrue(paths.contains(testDir.resolve("dir/subdir/file4.java")));
    }

    @Test
    public void testNoMatchingFiles() {
        List<Path> paths = pathGetter.getPaths(prevTestDir + "/**/*.md");
        assertTrue(paths.isEmpty());
    }

    private static Stream<Arguments> provideDataForTestGetPaths() {
        return Stream.of(
            Arguments.of("/file1.txt", 1),
            Arguments.of("/file2.log", 1),
            Arguments.of("/*.txt", 1),
            Arguments.of("/*.log", 1),
            Arguments.of("/**/*.java", 1),
            Arguments.of("/**/*.md", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForTestGetPaths")
    public void testGetPaths(String pattern, int expectedCount) {
        List<Path> paths = pathGetter.getPaths(testDir + pattern);
        assertEquals(expectedCount, paths.size());
    }

    @AfterAll
    public static void setDown() throws IOException {
        try (Stream<Path> pathStream = Files.walk(prevTestDir)) {
            pathStream.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}
