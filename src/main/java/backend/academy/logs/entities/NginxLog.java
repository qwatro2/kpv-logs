package backend.academy.logs.entities;

import java.time.LocalDateTime;
import java.util.Set;

public record NginxLog(
    String remoteAddress,
    String remoteUser,
    LocalDateTime timeLocal,
    Request request,
    Integer status,
    Integer bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {
    public static Set<String> getSetOfField() {
        return Set.of(
            "remote-address",
            "remote-user",
            "time-local",
            "request-type",
            "rout",
            "http-version",
            "status",
            "body-bytes-sent",
            "http-referer",
            "http-user-agent"
        );
    }
}
