package ru.am.scheduleapp.service.v2;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.repository.v2.EventRepository;
import ru.am.scheduleapp.repository.v2.WeekRepository;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final EventRepository eventRepository;
    private final WeekRepository weekRepository;

    public List<Week> getSchedule(Map<String, String> params) {

        // TODO filter by params
//        return weekDocumentV2Repository.findAll()
//                .flatMap(week -> eventDocumentV2Repository
//                        .findAllByDateBetween(week.getDateFrom(), week.getDateTo())
//                        .collectList()
//                        .defaultIfEmpty(List.of())
//                        .doOnNext(week::setEventDocumentV2List)
//                        .thenReturn(week));

        return List.of();
    }
}
