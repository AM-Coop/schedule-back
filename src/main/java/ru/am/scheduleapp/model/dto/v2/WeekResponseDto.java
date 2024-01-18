package ru.am.scheduleapp.model.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeekResponseDto {

    private UUID id;

    //    @Indexed(unique = true)
    private Integer num;


    private LocalDate dateFrom;
    private LocalDate dateTo;


    private String quote;


    private String note1;
    private String note2;


    private String community;

    private List<EventResponseDto> eventList;

}
