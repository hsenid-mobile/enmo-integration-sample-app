/*
 * (C) Copyright 2010-2022 hSenid Mobile Solutions (Pvt) Limited.
 * All Rights Reserved.
 *
 * This is only a SAMPLE CODE.
 *
 *  This code is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE.
 */

package hms.enmo.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Component
public class EnmoConnector {

    @Value("${enmo.api-url}")
    private String enmoApiUrl;


    @Autowired
    private WebClient webClient;

    private final String UNEXPECTED_ERROR = """
            {
              "errors": [
                {
                  "statusCode": "E9999",
                  "description": "Unexpected Error",
                  "message": "Unexpected Error",
                  "locations": [],
                  "errorType": "DataFetchingException",
                  "path": null,
                  "extensions": null
                }
              ]
            }
            """;

    public void sendEnmoRichTextContent() {
        var bodyColor = "#D1F6F3";
        var textColor = "yellow";
        var enmoAppId = 85;
        var requestBodyTemplate = """
                '{'
                  "operationName": "CreateContent",
                  "variables": '{'
                    "appId": {0},
                    "multiMediaContent": "",
                    "basicDetails": '{'
                      "templateText": "{1}",
                      "description": "{2}",
                      "backgroundColor": "{3}",
                      "enableComments": false,
                      "enableClickToCall": false,
                      "enableLikes": false,
                      "valid": false,
                      "fromDate": "",
                      "toDate": "",
                      "backgroundGraphic": "",
                      "textColor": "{4}",
                      "contentType": "TEXT_TEMPLATE",
                      "status": "approval_not_required"
                    '}'
                  '}',
                    "query": "mutation CreateContent($appId: Int!, $multiMediaContent: String!, $basicDetails: CreateContentReqGqlInput!) '{'\\n  createContent(appId: $appId, multiMediaContent: $multiMediaContent, basicDetails: $basicDetails) '{'\\n    contentId\\n    __typename\\n  '}'\\n'}'\\n"
                    '}'
                """;
        var requestBody = MessageFormat.format(requestBodyTemplate,
                enmoAppId,
                "Hello Enmo",
                "My first message with Enmo",
                textColor,
                bodyColor);

        Mono<String> response = webClient.post()
                .uri(enmoApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestBody), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume((Throwable th) -> {
                    System.out.println("ERROR==============>>>" + th);
                    return Mono.just(UNEXPECTED_ERROR);
                });

        response.subscribe((String resp) -> {
            if (resp.contains("errors")) {
                System.out.println("Content creation failed [%s]".formatted(resp));
            } else {
                System.out.println("Content creation successful [%s]".formatted(resp));
            }
        });

    }
}
