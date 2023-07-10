package org.dixie.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.dixie.image.Converter;
import org.dixie.image.TextGraphicsConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class GServer {
    public static final int PORT = 8888;

    private final HttpServer server;
    private final TextGraphicsConverter converter;

    public GServer(TextGraphicsConverter converter) throws Exception {
        if (converter == null) {
            throw new IllegalArgumentException("Converter can't be null");
        }
        this.converter = converter;
        this.converter.setMaxHeight(Converter.HEIGHT_CEILING);
        this.converter.setMaxWidth(Converter.WIDTH_CEILING);
        this.converter.setMaxRatio(Converter.RATIO_CEILING);

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/", this::serveHtml);
        server.createContext("/convert", this::serveConversion);
    }

    public void start() {
        System.out.println("Port: " + PORT);
        System.out.println("Browser: http://localhost:8888/");
        server.start();
    }

    protected void serveHtml(HttpExchange h) throws IOException {
        var htmlPath = Path.of("assets/index.html");
        var htmlContent = Files.readString(htmlPath);
        var jsPath = Path.of("assets/my.js");
        var jsContent = Files.readString(jsPath);
        htmlContent = htmlContent.replace("{{{JS}}}", jsContent);
        var htmlBytes = htmlContent.getBytes();
        h.sendResponseHeaders(200, htmlBytes.length);
        h.getResponseBody().write(htmlBytes);
        h.close();
    }

    protected void serveConversion(HttpExchange h) throws IOException {
        System.out.println("Convert request..");
        var url = new BufferedReader(new InputStreamReader(h.getRequestBody())).readLine();
        try {
            System.out.println("Converting image: " + url);
            var img = converter.convert(url).getBytes();
            System.out.println("...converted!");
            h.sendResponseHeaders(200, img.length);
            h.getResponseBody().write(img);
        } catch (Exception e) {
            e.printStackTrace();
            var msg = e.getMessage();
            if (msg.isEmpty()) {
                msg = "Conversion error";
            }
            var msgBytes = msg.getBytes();
            h.sendResponseHeaders(500, msgBytes.length);
            h.getResponseBody().write(msgBytes);
        } finally {
            h.close();
        }
    }
}
