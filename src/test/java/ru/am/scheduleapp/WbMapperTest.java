package ru.am.scheduleapp;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.document.v2.EventDocumentV2;

import java.io.InputStream;
import java.util.List;

import static ru.am.scheduleapp.utils.WbMapperUtils.readFromWb;


@Slf4j
public class WbMapperTest {

    @Test
    public void test() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("AM_Schedule_test.xlsx");
        try (ReadableWorkbook wb = new ReadableWorkbook(resourceAsStream)) {
            List<EventDocumentV2> docs = readFromWb(wb);
            System.out.println(docs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
