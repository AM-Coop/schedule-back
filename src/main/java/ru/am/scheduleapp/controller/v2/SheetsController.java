package ru.am.scheduleapp.controller.v2;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.am.scheduleapp.model.dto.v2.BasicResponse;
import ru.am.scheduleapp.service.v2.SheetsService;

@RestController
@AllArgsConstructor
@Slf4j
public class SheetsController {

    private final SheetsService sheetsService;

    @PostMapping("sheet/upload")
    @SneakyThrows
    public ResponseEntity<BasicResponse> upload(@RequestPart MultipartFile file) {
        sheetsService.operateNew(file);
        return ResponseEntity.ok(new BasicResponse("ok"));

    }


}