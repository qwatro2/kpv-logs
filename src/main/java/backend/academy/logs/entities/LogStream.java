package backend.academy.logs.entities;

import java.util.List;
import java.util.stream.Stream;

public record LogStream(List<String> names, Stream<String> stream) {}
