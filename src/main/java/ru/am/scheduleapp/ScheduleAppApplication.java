package ru.am.scheduleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.am.scheduleapp.configuration.properties.RsaProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaProperties.class)
public class ScheduleAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleAppApplication.class, args);
    }

}
