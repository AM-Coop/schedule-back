package ru.am.scheduleapp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.document.v2.EventDocumentV2;
import ru.am.scheduleapp.model.document.v2.Location;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class WbMapperTest {

    @Test
    public void test() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("AM_Schedule_test.xlsx");
        try (ReadableWorkbook wb = new ReadableWorkbook(resourceAsStream)) {
            List<EventDocumentV2> docs = getFromWb(wb);
            System.out.println(docs);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

//    id = eventSheet.read().get(0).getCell(0).getRawValue() // "id"

    @SneakyThrows
    private List<EventDocumentV2> getFromWb(ReadableWorkbook wb) {
        var eventSheet = wb.getSheet(0).get();
        var weekSheet = wb.getSheet(1).get();
        var eventManagerSheet = wb.getSheet(2).get();
        var locationSheet = wb.getSheet(3).get();
        var roomsSheet = wb.getSheet(4).get();

        List<Location> locations = toLocationList(locationSheet);
        //TODO остальные листы
        System.out.println(locations);

        return List.of();
    }

    private boolean canOperate(Row row) {
        boolean res = false;
        for (int i = 0; i < row.getCellCount(); i++) {
            String rawValue = Optional.ofNullable(row.getCell(i)).map(Cell::getRawValue).orElse(null);
            res = res || (rawValue != null && !rawValue.isEmpty());
        }
        return res;
    }

    @SneakyThrows
    private List<Location> toLocationList(Sheet locationSheet) {
        return locationSheet.read().stream().filter(this::canOperate).skip(1).map(row -> {
                    var id = (BigDecimal) row.getCell(0).getValue();
                    var name = getRawOrNull(row, 1);
                    var timeZone = getRawOrNull(row, 2);
                    var address = getRawOrNull(row, 3);
                    var rout = getRawOrNull(row, 4);
                    var icon = getRawOrNull(row, 5);
                    var description = getRawOrNull(row, 6);
                    return new Location(
                            id.intValue(), name, timeZone, address, rout, icon, description
                    );
                }
        ).toList();
    }

    private String getRawOrNull(Row row, int i) {
        return Optional.ofNullable(row.getCell(i)).map(Cell::getRawValue).orElse(null);
    }

}
