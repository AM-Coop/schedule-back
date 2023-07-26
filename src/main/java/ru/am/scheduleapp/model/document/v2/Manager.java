package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Manager {
    @Id
    private String id;
    private String name;
    private String photo;
    private String description;
    private String contact;
}
