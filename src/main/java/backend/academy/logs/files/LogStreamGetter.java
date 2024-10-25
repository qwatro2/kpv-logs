package backend.academy.logs.files;

import backend.academy.logs.entities.LogStream;
import java.util.List;

public interface LogStreamGetter {
    LogStream getStream(String path);
}
