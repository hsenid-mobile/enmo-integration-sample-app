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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    /**
     * Configure the webclient to work with OAuth
     * @param authorizedClientManager
     * @return
     */
    @Bean
    public WebClient webClient(final ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        final String resourceId = "enmo-client";


        final HttpClient httpClient  = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .doOnConnected(connection -> {
                    connection.addHandler(new ReadTimeoutHandler(10))
                            .addHandlerLast(new WriteTimeoutHandler(10));
                });

        final ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId(resourceId);
        return WebClient.builder()
                .filter(oauth)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
