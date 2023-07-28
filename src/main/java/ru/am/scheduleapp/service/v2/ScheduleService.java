package ru.am.scheduleapp.service.v2;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.am.scheduleapp.model.document.v2.EventDocumentV2;
import ru.am.scheduleapp.repository.v2.EventDocumentV2Repository;

import java.util.Map;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final EventDocumentV2Repository eventDocumentV2Repository;

    public Flux<EventDocumentV2> getSchedule(Map<String, String> params) {

        // TODO filter by params
        return eventDocumentV2Repository.findAll();
    }
}
