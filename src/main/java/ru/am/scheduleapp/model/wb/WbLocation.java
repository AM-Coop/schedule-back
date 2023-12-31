package ru.am.scheduleapp.model.wb;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WbLocation {

    private Integer num;
    private String name;
    private String timeZone;
    private String address;
    private String rout;

    private String region;
    private String icon;
    private String description;
}
