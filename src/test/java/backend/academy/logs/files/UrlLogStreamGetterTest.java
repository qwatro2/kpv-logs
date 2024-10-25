package backend.academy.logs.files;

import backend.academy.logs.entities.LogStream;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class UrlLogStreamGetterTest {

    @Test
    public void testGetStreamSuccessful() throws Exception {
        String url = "http://example.com/logs";
        String logData = "Line 1\nLine 2\nLine 3";

        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<Object> mockResponse = mock(HttpResponse.class);

        InputStream inputStream = new ByteArrayInputStream(logData.getBytes());
        Mockito.when(mockResponse.body()).thenReturn(inputStream);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockClient.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenReturn(mockResponse);

        UrlLogStreamGetter logStreamGetter = new UrlLogStreamGetter(mockClient);
        LogStream logStream = logStreamGetter.getStream(url);

        List<String> lines = logStream.stream().collect(Collectors.toList());
        assertEquals(List.of("Line 1", "Line 2", "Line 3"), lines);
        assertEquals(List.of(url), logStream.names());
    }

    @Test
    public void testGetStreamIOException() throws Exception {
        String url = "http://example.com/logs";

        HttpClient mockClient = mock(HttpClient.class);
        Mockito.when(mockClient.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenThrow(new IOException("Network error"));

        UrlLogStreamGetter logStreamGetter = new UrlLogStreamGetter(mockClient);
        LogStream logStream = logStreamGetter.getStream(url);

        assertTrue(logStream.names().isEmpty());
        assertTrue(logStream.stream().findAny().isEmpty());
    }

    @Test
    public void testGetStreamInterruptedException() throws Exception {
        String url = "http://example.com/logs";

        HttpClient mockClient = mock(HttpClient.class);
        Mockito.when(mockClient.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenThrow(new InterruptedException("Request interrupted"));

        UrlLogStreamGetter logStreamGetter = new UrlLogStreamGetter(mockClient);
        LogStream logStream = logStreamGetter.getStream(url);

        assertTrue(logStream.names().isEmpty());
        assertTrue(logStream.stream().findAny().isEmpty());
    }
}
