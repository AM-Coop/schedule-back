package ru.am.scheduleapp.service.v2;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.am.scheduleapp.model.document.v2.WeekDocumentV2;
import ru.am.scheduleapp.repository.v2.EventDocumentV2Repository;
import ru.am.scheduleapp.repository.v2.WeekDocumentV2Repository;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final EventDocumentV2Repository eventDocumentV2Repository;
    private final WeekDocumentV2Repository weekDocumentV2Repository;

    public Flux<WeekDocumentV2> getSchedule(Map<String, String> params) {

        // TODO filter by params
        return weekDocumentV2Repository.findAll()
                .flatMap(week -> eventDocumentV2Repository
                        .findAllByDateBetween(week.getDateFrom(), week.getDateTo())
                        .collectList()
                        .defaultIfEmpty(List.of())
                        .doOnNext(week::setEventDocumentV2List)
                        .thenReturn(week));
    }
}
