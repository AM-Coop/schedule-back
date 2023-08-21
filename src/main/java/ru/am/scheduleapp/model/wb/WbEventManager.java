package ru.am.scheduleapp.model.wb;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WbEventManager implements WbObj {

    private Integer id;
    private String name;
    private String photo;
    private String description;
    private String contact;

}
