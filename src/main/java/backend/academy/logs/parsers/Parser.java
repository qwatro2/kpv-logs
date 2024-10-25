package backend.academy.logs.parsers;

public interface Parser<From, To> {
    To parse(From value);
}
