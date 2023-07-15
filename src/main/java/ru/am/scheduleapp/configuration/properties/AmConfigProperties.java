package ru.am.scheduleapp.configuration.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ru.am.google-tabs.auth-file-path")
@Getter
@Setter
@ToString
public class AmConfigProperties {

    private String unix;
    private String win;
    private String googleAuthFilePath;

    @PostConstruct
    public void init() {
        var os = System.getProperty("os.name");
        if (os.contains("win")) googleAuthFilePath = win;
        else googleAuthFilePath = unix;
    }

}
