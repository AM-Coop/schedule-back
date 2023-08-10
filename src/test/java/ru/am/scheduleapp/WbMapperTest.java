package ru.am.scheduleapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.utils.WbMapperUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@Slf4j
public class WbMapperTest {

//    @Test
//    public void test() {
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("AM_Schedule_test.xlsx");
//        try (ReadableWorkbook wb = new ReadableWorkbook(resourceAsStream)) {
//            var docs = readFromWb(wb);
//            System.out.println(docs);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }


    @Test
    public void testFilterActualWeeks() {
        List<Week> acts = Stream.of(
                new Week(UUID.randomUUID(), 1, null, null, LocalDate.now(), LocalDate.now().plusDays(7), List.of()),
                new Week(UUID.randomUUID(), 2, null, null, LocalDate.now().plusDays(3), LocalDate.now().plusDays(10), List.of()),
                new Week(UUID.randomUUID(), 3, null, null, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3), List.of())
        ).filter(WbMapperUtils::isActualWeek).toList();

        System.out.println(acts);
        Assertions.assertEquals(acts.get(0).getNum(), 1);
        Assertions.assertEquals(acts.get(1).getNum(), 2);

    }


}
