package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventManager {
    private Integer num;
    private String managerName;
    private String image;
    private String managerDescription;
    private String managerContact;
}
