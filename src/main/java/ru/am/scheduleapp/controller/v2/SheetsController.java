package ru.am.scheduleapp.controller.v2;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@AllArgsConstructor
@Slf4j
public class SheetsController {

    @PostMapping("/")
    @SneakyThrows
    public Mono<ResponseEntity<String>> upload(@RequestPart Mono<FilePart> file) {
//        PipedOutputStream outputStream = new PipedOutputStream();
//        PipedInputStream inputStream = new PipedInputStream(1024*10)
//        inputStream.connect(outputStream);
        var loc = new AtomicReference<String>();
        return file.doOnNext(f -> System.out.println(f.filename()))
                .flatMap(f -> {
                    String pathname = "data/" + f.filename();
                    loc.set(pathname);
                    File dest = new File(pathname);
                    try {
                        dest.createNewFile(); //todo in non blocking way
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    return f.transferTo(dest);
                })
                .then(Mono.defer(() -> {
                    try (FileInputStream fis = new FileInputStream(loc.get());
                         ReadableWorkbook wb = new ReadableWorkbook(fis)) {
                        System.out.println();
                        //todo
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    return Mono.just("ok");
                }))
                .thenReturn(ResponseEntity.ok("ok"));
    }

    private void operateWb() {

    }

}