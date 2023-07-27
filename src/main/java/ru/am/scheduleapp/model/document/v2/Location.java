package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZoneId;

@Data
@AllArgsConstructor
public class Location {
    private Integer num;
    private String name;
    private String region;
    private ZoneId timeZone;
    private String address;
    private String rout;
    private String icon;
    private String description;
}
