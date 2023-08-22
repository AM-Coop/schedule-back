package ru.am.scheduleapp.service.v2;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.am.scheduleapp.model.dto.v2.WeekResponseDto;
import ru.am.scheduleapp.repository.v2.WeekRepository;
import ru.am.scheduleapp.utils.DtoMapperUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final WeekRepository weekRepository;
    private final SheetsService sheetsService;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            try {
                log.info("refresh tables after startup");
                refreshFromGoogleSheets();
            } catch (Exception e) {
                log.error("err -> {}", e.getMessage(), e);
            }
        }).start();
    }

    @Transactional
    public List<WeekResponseDto> getSchedule(Map<String, String> params) {

        // TODO filter by params
//        return weekDocumentV2Repository.findAll()
//                .flatMap(week -> eventDocumentV2Repository
//                        .findAllByDateBetween(week.getDateFrom(), week.getDateTo())
//                        .collectList()
//                        .defaultIfEmpty(List.of())
//                        .doOnNext(week::setEventDocumentV2List)
//                        .thenReturn(week));

        var res = new java.util.ArrayList<WeekResponseDto>(List.of());
        weekRepository.findAll().forEach(w -> res.add(DtoMapperUtils.mapFromWeekEntity(w)));
        return res;
    }

    public void refreshFromGoogleSheets() throws GeneralSecurityException, IOException {

        sheetsService.refreshFromGoogleSheets();


    }
}
