package backend.academy.logs.converters;

public interface Converter<T> {
    String convert(T value);
}
