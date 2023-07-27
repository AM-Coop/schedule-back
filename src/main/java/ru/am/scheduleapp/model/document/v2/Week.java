package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@AllArgsConstructor
@Data
public class Week {
    private Integer num;
    private String quote;
    private String notes;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
