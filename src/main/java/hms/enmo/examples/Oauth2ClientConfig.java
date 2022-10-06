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
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class Oauth2ClientConfig {

    @Bean
    ReactiveClientRegistrationRepository getRegistration(@Value("${enmo.auth-server.access-token-uri}") final String tokenUri,
                                                         @Value("${enmo.auth-server.client-id}") final String clientId
    ) {
        final ClientRegistration registration = ClientRegistration
                .withRegistrationId("enmo-client")
                .tokenUri(tokenUri)
                .clientId(clientId)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService auth2AuthorizedClientService(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(final ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                                         final ReactiveOAuth2AuthorizedClientService authorizedClientService,
                                                                         @Value("${enmo.auth-server.username}") final String username,
                                                                         @Value("${enmo.auth-server.password}") final String password) {

        final ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .refreshToken()
                        .password()
                        .build();

        final AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper(username, password));

        return authorizedClientManager;
    }

    private Function<OAuth2AuthorizeRequest, Mono<Map<String, Object>>> contextAttributesMapper(final String username, final String password) {
        return authorizeRequest -> {
            final Map<String, Object> contextAttributes = new HashMap<>();
            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
            return Mono.just(contextAttributes);
        };
    }

}
