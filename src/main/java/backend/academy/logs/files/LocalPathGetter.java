package backend.academy.logs.files;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class LocalPathGetter {
    public List<Path> getPaths(String path) {
        List<Path> result = new ArrayList<>();

        String[] split = splitPath(path);

        if (split.length == 1) {
            result.add(Path.of(split[0]));
            return result;
        }

        String startDir = split[0];
        String pattern = split[1];

        Path startPath = Paths.get(startDir);
        String globPattern = "glob:" + pattern;
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(globPattern);

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (matcher.matches(startPath.relativize(file))) {
                        result.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {

        }

        return result;
    }

    private String[] splitPath(String path) {
        int patternIndex = path.indexOf('*');
        if (patternIndex == -1) {
            return new String[] {path};
        }

        String startDir = path.substring(0, patternIndex).replaceAll("/+$", "");
        String pattern = path.substring(patternIndex);
        return new String[] {startDir, pattern};
    }
}
