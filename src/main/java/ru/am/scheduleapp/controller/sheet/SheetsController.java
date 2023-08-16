package ru.am.scheduleapp.controller.sheet;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.am.scheduleapp.service.v2.SheetsService;

@RestController
@AllArgsConstructor
@Slf4j
public class SheetsController {

    private final SheetsService sheetsService;

//    @PostMapping("/sheet/upload")
//    @SneakyThrows
//    public ResponseEntity<BasicResponse> upload(@RequestPart MultipartFile file) {
//        sheetsService.operateNew(file);
//        return ResponseEntity.ok(new BasicResponse("ok"));
//
//    }


}