package backend.academy.logs.files;

import backend.academy.logs.entities.LogStream;

public interface LogStreamGetter {
    LogStream getStream(String path);
}
