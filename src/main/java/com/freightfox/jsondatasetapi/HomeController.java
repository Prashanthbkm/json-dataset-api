
package com.freightfox.jsondatasetapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
                <html>
                <head>
                    <title>JSON Dataset API</title>
                </head>
                <body style="font-family: Arial; text-align: center; margin-top: 100px;">
                    <h1>Welcome to JSON Dataset API</h1>
                    <p>Your Spring Boot application is running successfully!</p>
                    <a href="/swagger-ui/index.html">Open API Documentation (Swagger)</a>
                </body>
                </html>
                """;
    }
}
