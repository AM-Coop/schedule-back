package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class WeekDocumentV2 {
    @Id
    private String id;

    @Indexed(unique = true)
    private Integer num;
    private String quote;
    private String notes;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @Transient
    private List<EventDocumentV2> eventDocumentV2List = List.of();
}
