package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private Integer id;
    private String name;
    private String timeZone;
    private String address;
    private String rout;
    private String icon;
    private String description;
}
