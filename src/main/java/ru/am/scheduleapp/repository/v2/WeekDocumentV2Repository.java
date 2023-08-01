package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.am.scheduleapp.model.document.v2.WeekDocumentV2;

import java.time.LocalDate;

@Repository
public interface WeekDocumentV2Repository extends ReactiveCrudRepository<WeekDocumentV2, String> {

    public Mono<WeekDocumentV2> findWeekDocumentV2ByDateFromAndDateTo(LocalDate dateFrom, LocalDate dateTo);

}
