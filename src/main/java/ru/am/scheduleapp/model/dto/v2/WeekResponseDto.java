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


    private String note1;
    private String note2;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    private String community;


    private List<EventResponseDto> eventList;
}
