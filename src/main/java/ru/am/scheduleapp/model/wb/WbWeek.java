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

    private String note1;
    private String note2;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private String community;


}
