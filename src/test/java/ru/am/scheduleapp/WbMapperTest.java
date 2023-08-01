package ru.am.scheduleapp;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.document.v2.WeekDocumentV2;
import ru.am.scheduleapp.utils.WbMapperUtils;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static ru.am.scheduleapp.utils.WbMapperUtils.readFromWb;


@Slf4j
public class WbMapperTest {

    @Test
    public void test() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("AM_Schedule_test.xlsx");
        try (ReadableWorkbook wb = new ReadableWorkbook(resourceAsStream)) {
            var docs = readFromWb(wb);
            System.out.println(docs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @Test
    public void testFilterActualWeeks() {
        List<WeekDocumentV2> acts = Stream.of(
                new WeekDocumentV2("1", 1, null, null, LocalDate.now(), LocalDate.now().plusDays(7), List.of()),
                new WeekDocumentV2("2", 2, null, null, LocalDate.now().plusDays(3), LocalDate.now().plusDays(10), List.of()),
                new WeekDocumentV2("3", 3, null, null, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3), List.of())
        ).filter(WbMapperUtils::isActualWeek).toList();

        System.out.println(acts);
        Assertions.assertEquals(acts.get(0).getNum(), 1);
        Assertions.assertEquals(acts.get(1).getNum(), 2);

    }


}
