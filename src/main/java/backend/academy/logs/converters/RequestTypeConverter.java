package backend.academy.logs.converters;

import backend.academy.logs.types.RequestType;

public class RequestTypeConverter implements Converter<RequestType> {
    @Override
    public String convert(RequestType value) {
        return value.toString();
    }
}
