package ru.am.scheduleapp.model.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class WeekResponseDto {

    private UUID id;

    //    @Indexed(unique = true)
    private Integer num;


    private String quote;


    private String notes;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    private List<EventResponseDto> eventList;
}
