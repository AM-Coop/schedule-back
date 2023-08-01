package ru.am.scheduleapp.controller.v2;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.am.scheduleapp.service.v2.SheetsService;

@RestController
@AllArgsConstructor
@Slf4j
public class SheetsController {

    private final SheetsService sheetsService;

    @PostMapping("/")
    @SneakyThrows
    public Mono<ResponseEntity<String>> upload(@RequestPart Mono<FilePart> file) {

        return sheetsService.operateNew(file);

    }



}