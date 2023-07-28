package ru.am.scheduleapp.model.document.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Document
@AllArgsConstructor
@Data
public class EventDocumentV2 {
    @Id
    private String id;
    private Integer num;
    private String title;
    private Location location;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ZoneId timeZone;
    private String description;
    private EventManager manager;
    private boolean paid;
    private BigDecimal paymentAmount;
    private boolean boldAm;
    private boolean boldUm;
    private boolean suitableUm;
    private boolean publish;
    private Week week;
}
