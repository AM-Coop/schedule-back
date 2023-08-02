package ru.am.scheduleapp.controller.v2;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.am.scheduleapp.model.document.v2.WeekDocumentV2;
import ru.am.scheduleapp.service.v2.ScheduleService;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/v2")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public Flux<WeekDocumentV2> getSchedule(
            @RequestParam Map<String, String> params
    ) {

        return scheduleService.getSchedule(params);
    }
}
