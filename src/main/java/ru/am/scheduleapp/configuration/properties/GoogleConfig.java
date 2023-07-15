package ru.am.scheduleapp.configuration.properties;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GoogleConfig {

    private final AmConfigProperties amConfigProperties; // можно делать так либо

//    @Value("ru.am.google-tabs.auth-file-path.unix") либо так вместо AmConfigProperties, но с конфигами приятнее выглядит
//    private String unixPath;

    @Value("spring.application.name")
    private String appName;

    @Bean
    @SneakyThrows
    public Sheets sheets() {
        try (InputStream is = new FileInputStream(
                amConfigProperties.getGoogleAuthFilePath()
        )) {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleCredentials credentials = GoogleCredentials.fromStream(is);
            if (credentials.createScopedRequired()) {
                credentials = credentials.createScoped(SheetsScopes.SPREADSHEETS_READONLY);
            }

            return new Sheets.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                    .setApplicationName(appName)
                    .build();
        } catch (IOException ex) {
            log.error("err -> {}", ex.getMessage(), ex);
        }
        throw new RuntimeException("что-то пошло не так");
    }

}
