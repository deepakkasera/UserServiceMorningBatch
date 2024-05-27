package com.example.userservicemorningbatch;

import com.example.userservicemorningbatch.security.repositories.JpaRegisteredClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.test.annotation.Commit;

import java.util.UUID;

//@SpringBootTest
class UserServiceMorningBatchApplicationTests {
//    @Autowired
//    private JpaRegisteredClientRepository jpaRegisteredClientRepository;

    @Test
    void contextLoads() {
    }

//    @Test
//    @Commit
//    void storeRegisteredClientInDB() {
//        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("oidc-client")
//                .clientSecret("$2a$12$y/UcEklUz4D2OAM1jplV5e0evGzp0NqeZhdWfD8Pxz7cO5xk7rPjS")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .redirectUri("https://oauth.pstmn.io/v1/callback")
//                .postLogoutRedirectUri("https://oauth.pstmn.io/v1/callback")
//                .scope(OidcScopes.OPENID) // Roles
//                .scope(OidcScopes.PROFILE)
//                .scope("ADMIN")
////                .scope("MENTOR")
////                .scope("TA")
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .build();
//
//        jpaRegisteredClientRepository.save(oidcClient);
//    }

}


