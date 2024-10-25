package backend.academy.logs.files;

import backend.academy.logs.entities.LogStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Stream;

public class UrlLogStreamGetter implements LogStreamGetter {
    private final HttpClient httpClient;

    public UrlLogStreamGetter() {
        this(HttpClient.newHttpClient());
    }

    protected UrlLogStreamGetter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public LogStream getStream(String path) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(path))
            .GET()
            .build();
        HttpResponse<InputStream> response;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        } catch (InterruptedException | IOException e) {
            return new LogStream(List.of(), Stream.of());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));
        return new LogStream(List.of(path), reader.lines());
    }
}
