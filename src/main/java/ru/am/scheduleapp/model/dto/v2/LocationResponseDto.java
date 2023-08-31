package ru.am.scheduleapp.model.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class LocationResponseDto {


    private UUID id;

    private Integer num;

    private String name;

    private String region;
    private String timeZone;
    private String address;
    private String rout;
    private String icon;
    private String description;

    private Integer order;

}
