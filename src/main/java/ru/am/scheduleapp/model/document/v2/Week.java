package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class Week {
    @Id
    private String id;
    private String quote;
    private String notes;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
