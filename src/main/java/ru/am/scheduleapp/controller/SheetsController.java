package ru.am.scheduleapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class SheetsController {

    @PostMapping("/")
    public Mono<ResponseEntity<String>> upload(@RequestPart Mono<FilePart> file) {
        return file.doOnNext(f -> System.out.println(f.filename()))
                .thenReturn(ResponseEntity.ok("ok"));
    }
}