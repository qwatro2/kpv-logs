package backend.academy.logs.parsers;

import backend.academy.logs.entities.Request;

public interface RequestParser {
    Request parse(String request);
}
