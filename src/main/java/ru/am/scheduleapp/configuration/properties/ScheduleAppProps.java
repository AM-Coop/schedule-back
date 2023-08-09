package ru.am.scheduleapp.configuration.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("ru.am.scheduleapp")
@Data
@NoArgsConstructor
public class ScheduleAppProps {
    private boolean useDateFilter;
    private String wbDirLocation;
}
