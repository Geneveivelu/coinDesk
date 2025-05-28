package com.example.demo.utils;

import com.example.demo.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class HttpClientUtils {
  private static final Logger logger = LogManager.getLogger();

  private static final HttpClient httpClient;

  private static final int MAX_POOL_CONNECTIONS = 150;

  static {
    ExecutorService executorService = Executors.newFixedThreadPool(MAX_POOL_CONNECTIONS);

    httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .executor(executorService)
        .build();
  }

  public static HttpResponse<String> get(String url) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(url))
          .GET()
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info("Request {}, status: {}, response: {}", url, response.statusCode(), response.body());
      return response;
    } catch (Exception e) {
      logger.error("Get url:{} error", url, e);
      throw new RuntimeException("Get request error", e);
    }
  }

  public static HttpResponse<String> post(String url, Map<String, String> headers) {
    return post(url, headers, null);
  }

  public static HttpResponse<String> post(String url, Map<String, String> headers, Object requestBody) {
    String[] headerArray = getHeaderArray(headers);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();
    if (requestBody != null) {
      String requestBodyString = JsonUtils.toJson(requestBody);
      bodyPublisher = HttpRequest.BodyPublishers.ofString(requestBodyString);
      logger.info("requestBody : {} ", requestBodyString);
    }

    try {
      HttpRequest request = HttpRequest.newBuilder()
          .headers(headerArray)
          .uri(new URI(url))
          .POST(bodyPublisher)
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info("Request {}, status: {}, response: {}", url, response.statusCode(), response.body());
      return response;
    } catch (Exception e) {
      logger.error("Post url:{} error", url, e);
      throw new RuntimeException("Post request error", e);
    }
  }

  private static String[] getHeaderArray(Map<String, String> headers) {
    return headers.entrySet().stream()
        .flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
        .toArray(String[]::new);
  }

}
