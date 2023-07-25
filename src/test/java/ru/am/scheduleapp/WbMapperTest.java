package ru.am.scheduleapp;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

@Slf4j
public class WbMapperTest {

    @Test
    public void test() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("test.xlsx");
        try (ReadableWorkbook wb = new ReadableWorkbook(resourceAsStream)) {
            var eventSheet = wb.getSheet(0).get();
            var weekSheet = wb.getSheet(1).get();
            var eventManagerSheet = wb.getSheet(2).get();
            var locationSheet = wb.getSheet(3).get();
            var roomsSheet = wb.getSheet(4).get();
            System.out.println();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
