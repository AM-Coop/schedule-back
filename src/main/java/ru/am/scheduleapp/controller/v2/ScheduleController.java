package ru.am.scheduleapp.controller.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.am.scheduleapp.model.dto.v2.BasicResponse;
import ru.am.scheduleapp.model.dto.v2.WeekResponseDto;
import ru.am.scheduleapp.service.v2.ScheduleService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class ScheduleController {

    private final AtomicReference<LocalDateTime> lastRefreshTime = new AtomicReference<>(LocalDateTime.now().minusSeconds(55));

    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    @CrossOrigin(origins = "http://schedule.rishathm.ru")
    public ResponseEntity<List<WeekResponseDto>> getSchedule(
            @RequestParam Map<String, String> params
    ) {

        return ResponseEntity.ok(scheduleService.getSchedule(params));
    }

    @GetMapping("/refresh")
    public ResponseEntity<BasicResponse> refresh() throws GeneralSecurityException, IOException {
        if (lastRefreshTime.get().plusMinutes(1).isAfter(LocalDateTime.now()))
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new BasicResponse("to early"));
        else {
            scheduleService.refreshFromGoogleSheets(true);
            lastRefreshTime.set(LocalDateTime.now());
            return ResponseEntity.ok(new BasicResponse("ok"));
        }
    }
}
