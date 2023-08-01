package ru.am.scheduleapp.service.v2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.am.scheduleapp.model.document.v2.EventDocumentV2;
import ru.am.scheduleapp.model.document.v2.WeekDocumentV2;
import ru.am.scheduleapp.repository.v2.EventDocumentV2Repository;
import ru.am.scheduleapp.repository.v2.WeekDocumentV2Repository;
import ru.am.scheduleapp.utils.WbMapperUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Slf4j
public class SheetsService {
    private final EventDocumentV2Repository eventDocumentV2Repository;
    private final WeekDocumentV2Repository weekDocumentV2Repository;

    public Mono<ResponseEntity<String>> operateNew(Mono<FilePart> file) {
        final AtomicReference<String> loc = new AtomicReference<String>();
        return file.doOnNext(f -> System.out.println(f.filename()))
                .flatMap(f -> saveFile(f, loc))
                .then(operateFile(loc))
                .thenReturn(ResponseEntity.ok("ok"));
    }

    private Mono<String> operateFile(AtomicReference<String> loc) {
        return Mono.defer(() -> {
            try (FileInputStream fis = new FileInputStream(loc.get());
                 ReadableWorkbook wb = new ReadableWorkbook(fis)) {
                return operateWb(wb).thenReturn("ok");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return Mono.just("ok");
        });
    }

    private Mono<Void> saveFile(FilePart f, AtomicReference<String> loc) {
        String pathname = "data/" + f.filename();
        loc.set(pathname);
        File dest = new File(pathname);
        try {
            dest.createNewFile(); //todo in non blocking way
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return f.transferTo(dest);
    }

    private Mono<Void> operateWb(ReadableWorkbook wb) throws IOException {
        Tuple2<List<EventDocumentV2>, List<WeekDocumentV2>> tuple = WbMapperUtils.readFromWb(wb);
        return updateEvents(tuple.getT1()).zipWith(updateWeeks(tuple.getT2())).then();
    }

    private Mono<Void> updateEvents(List<EventDocumentV2> events) {
        var now = LocalDate.now();
        return Flux.fromIterable(events)
                .filter(it -> it.getDate().isEqual(now) || it.getDate().isAfter(now))
                .flatMap(it -> eventDocumentV2Repository.findByNum(it.getNum())
                        .switchIfEmpty(Mono.just(it)).doOnNext(next -> log.info("no event {} was found", next)))
                .flatMap(eventDocumentV2Repository::save)
                .then();
    }

    private Mono<Void> updateWeeks(List<WeekDocumentV2> weeks) {
        return Flux.fromIterable(weeks)
                .filter(WbMapperUtils::isActualWeek)
                .flatMap(it ->
                        weekDocumentV2Repository.findWeekDocumentV2ByDateFromAndDateTo(it.getDateFrom(), it.getDateTo())
                                .switchIfEmpty(Mono.just(it).doOnNext(newWeek -> log.info("no week {} was found", it)))
                )
                .flatMap(weekDocumentV2Repository::save)
                .then();
    }


}
