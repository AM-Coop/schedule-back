package ru.am.scheduleapp.model.wb;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WbRoom {

    private Integer num;
    private String title;
    private String location;

}
