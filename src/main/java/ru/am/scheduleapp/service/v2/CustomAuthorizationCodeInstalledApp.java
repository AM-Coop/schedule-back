package ru.am.scheduleapp.service.v2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

import java.io.IOException;

public class CustomAuthorizationCodeInstalledApp extends AuthorizationCodeInstalledApp {
    public CustomAuthorizationCodeInstalledApp(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
        super(flow, receiver);
    }

    @Override
    protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
        System.out.println(authorizationUrl.build());
        super.onAuthorization(authorizationUrl);
    }
}
