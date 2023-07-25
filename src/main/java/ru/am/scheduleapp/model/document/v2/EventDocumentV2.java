package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Data
public class EventDocumentV2 {
    @Id
    private String id;

    private Integer num;


}
