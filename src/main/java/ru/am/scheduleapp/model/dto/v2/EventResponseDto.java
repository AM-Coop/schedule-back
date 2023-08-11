package ru.am.scheduleapp.model.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;

//@Document
@AllArgsConstructor
@Data
public class EventResponseDto {


    private UUID id;

    private Integer num;
    private String title;


    private LocationResponseDto location;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ZoneId timeZone;
    private String description;


    private ManagerResponseDto manager;
    private boolean paid;
    private BigDecimal paymentAmount;
    private boolean boldAm;
    private boolean boldUm;
    private boolean suitableUm;
    private boolean publish;


}
