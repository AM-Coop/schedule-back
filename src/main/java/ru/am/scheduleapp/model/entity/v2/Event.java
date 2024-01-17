package ru.am.scheduleapp.model.entity.v2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;

//@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedule_event")
public class Event {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    //    @Indexed(unique = true)
    private Integer num;
    private String title;

    @ManyToOne
    private Location location;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ZoneId timeZone;
    private String description;

    @ManyToOne
    private Manager manager;
    private boolean paid;
    private BigDecimal paymentAmount;
    private boolean boldAm;
    private boolean boldUm;
    private boolean suitableUm;

    private boolean suitableAm;
    private boolean publish;

    @ManyToOne(cascade = CascadeType.ALL)
    private Week week;

}
