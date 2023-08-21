package ru.am.scheduleapp.model.wb;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

//@Document
@AllArgsConstructor
@Data
public class WbWeek implements WbObj {


    private Integer num;


    private String quote;

    private String notes;
    private LocalDate dateFrom;
    private LocalDate dateTo;


}
