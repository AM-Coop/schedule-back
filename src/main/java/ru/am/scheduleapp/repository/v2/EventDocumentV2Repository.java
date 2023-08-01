package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.am.scheduleapp.model.document.v2.EventDocumentV2;

@Repository
public interface EventDocumentV2Repository extends ReactiveCrudRepository<EventDocumentV2, String> {

    public Mono<EventDocumentV2> findByNum(int num);

}
