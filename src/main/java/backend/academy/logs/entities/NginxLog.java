package backend.academy.logs.entities;

import java.time.LocalDateTime;
import java.util.List;

public record NginxLog(
    String remoteAddress,
    String remoteUser,
    LocalDateTime timeLocal,
    Request request,
    int status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {
    public static List<String> getListOfField() {
        return List.of(
            "remote-address",
            "remote-user",
            "time-local",
            "request-type",
            "rout",
            "http-version",
            "status",
            "body-bytes-send",
            "http-referer",
            "http-user-agent"
        );
    }
}
