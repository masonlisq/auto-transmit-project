package com.kfm.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;

public class ResponseUtils {
    /**
     * 连接服务器，向服务器发送请求，根据得到的响应判断服务器的状态
     * @param server 服务器地址
     * @return 服务器状态
     */
    public static int checkServerStatus(String server) {
        try {
            URL url = new URL(server);

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest httpRequest = HttpRequest.newBuilder(url.toURI())
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> httpResponse = httpClient
                    .send(httpRequest, HttpResponse
                            .BodyHandlers
                            .ofString(Charset.defaultCharset()));

            System.out.println("[ResponseUtils]"+httpResponse.statusCode()+" "+httpResponse.uri());
            return httpResponse.statusCode();


        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
