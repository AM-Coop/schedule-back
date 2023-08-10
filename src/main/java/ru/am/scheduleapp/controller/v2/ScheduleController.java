package ru.am.scheduleapp.controller.v2;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.service.v2.ScheduleService;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/v2")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public ResponseEntity<List<Week>> getSchedule(
            @RequestParam Map<String, String> params
    ) {

        return ResponseEntity.ok(scheduleService.getSchedule(params));
    }
}
