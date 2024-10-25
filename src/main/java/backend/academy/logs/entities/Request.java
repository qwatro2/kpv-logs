package backend.academy.logs.entities;

import backend.academy.logs.types.RequestType;

public record Request(
    RequestType requestType,
    String rout,
    String httpVersion
){}
