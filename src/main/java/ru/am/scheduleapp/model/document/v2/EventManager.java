package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class EventManager {
    @Id
    private String id;
    private String managerName;
    private String image;
    private String managerDescription;
    private String managerContact;
}
