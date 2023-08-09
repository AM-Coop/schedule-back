package ru.am.scheduleapp.model.wb;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

//@Document
@AllArgsConstructor
@Data
public class WbEvent {


    private Integer num;
    private String title;

    private String locationName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ZoneId timeZone;
    private String description;

    private String managerName;
    private boolean paid;
    private BigDecimal paymentAmount;
    private boolean boldAm;
    private boolean boldUm;
    private boolean suitableUm;
    private boolean publish;


}
