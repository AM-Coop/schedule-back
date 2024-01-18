package ru.am.scheduleapp.service.v2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.am.scheduleapp.model.dto.v2.WeekResponseDto;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.repository.v2.RegionDictRepository;
import ru.am.scheduleapp.repository.v2.WeekRepository;
import ru.am.scheduleapp.utils.DtoMapperUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final WeekRepository weekRepository;
    private final RegionDictRepository regionDictRepository;

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
//    @Cacheable("schedule")
    public List<WeekResponseDto> getSchedule(Map<String, String> params) {
        String regionCode = Optional.ofNullable(params.get("region")).orElse("MSC");


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

            var start = LocalDateTime.now()
                    .plusDays(daysOffset)
                    .plusHours(4); // Чтобы в вс после 20:00 уже показывать след неделю
            return (elem.getDateFrom().isBefore(start.toLocalDate()) || elem.getDateFrom().isEqual(start.toLocalDate())) &&
                    (elem.getDateTo().isAfter(start.toLocalDate()) || elem.getDateTo().isEqual(start.toLocalDate()));
        }).findFirst().orElse(current);

        return current != null ? List.of(DtoMapperUtils.mapFromWeekEntity(current, showAm, showUm, regionCode)) : List.of();
    }

//    public void refreshFromGoogleSheets(boolean refreshEntities) throws GeneralSecurityException, IOException {
//
//        sheetsService.refreshFromGoogleSheets(refreshEntities);
//        evictCache();
//
//    }
//
//    @CacheEvict(value = "schedule", allEntries = true)
//    public void evictCache() {
//
//    }
}
