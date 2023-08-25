package ru.am.scheduleapp.service.v2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.am.scheduleapp.model.dto.v2.WeekResponseDto;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.repository.v2.WeekRepository;
import ru.am.scheduleapp.utils.DtoMapperUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final WeekRepository weekRepository;
    private final SheetsService sheetsService;

//    @PostConstruct
//    public void init() {
//        readFromGoogle();
//
//        Flux.interval(Duration.ofMinutes(1))
//                .publishOn(Schedulers.parallel())
//                .doOnNext(next -> {
////                    log.info("start red from google");
//
//                    readFromGoogle();
////                    log.info("finish red from google");
//                })
////                .timeout(Duration.ofSeconds(20))
//                .doOnError(err -> log.error("err -> {}", err.getMessage(), err))
//                .subscribe();
//    }

//    private void readFromGoogle() {
//        try {
//            refreshFromGoogleSheets(false);
//        } catch (Exception e) {
//            log.error("err -> {}", e.getMessage(), e);
//        }
//    }

    @Transactional
    public List<WeekResponseDto> getSchedule(Map<String, String> params) {

        String community = Optional.ofNullable(params.get("community")).orElse("UM");

        boolean showAm = community.contains("AM");
        boolean showUm = community.contains("UM");


        Integer weekOffset = Optional.ofNullable(params.get("week_offset")).map(Integer::parseInt).orElse(0);
        var daysOffset = weekOffset * 7;

        // TODO сделать на уровне селектов
        List<Week> weeks = (List<Week>) weekRepository.findAll();

        Week current = null;
        // делаем дефолт, если данные старые
        if (!weeks.isEmpty()) {
            current = weeks.get(weeks.size() - 1);
        }

        current = weeks.stream().filter(elem -> {
            var start = LocalDate.now().plusDays(daysOffset);
            return (elem.getDateFrom().isBefore(start) || elem.getDateFrom().isEqual(start)) &&
                    (elem.getDateTo().isAfter(start) || elem.getDateTo().isEqual(start));
        }).findFirst().orElse(current);

        return current != null ? List.of(DtoMapperUtils.mapFromWeekEntity(current, showAm, showUm)) : List.of();
    }

    public void refreshFromGoogleSheets(boolean refreshEntities) throws GeneralSecurityException, IOException {

        sheetsService.refreshFromGoogleSheets(refreshEntities);


    }
}
