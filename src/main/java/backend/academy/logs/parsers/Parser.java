package backend.academy.logs.parsers;

public interface Parser<F, T> {
    T parse(F value);
}
