package ru.am.scheduleapp.model.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ManagerResponseDto {
    private UUID id;

    private Integer num;

    private String name;
    private String photo;
    private String description;
    private String contact;

    private String worldlyName;

}
