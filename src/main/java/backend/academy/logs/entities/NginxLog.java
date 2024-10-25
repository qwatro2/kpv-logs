package backend.academy.logs.entities;

import java.time.LocalDateTime;

public record NginxLog(
    String remoteAddress,
    String remoteUser,
    LocalDateTime timeLocal,
    Request request,
    int status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
){}
