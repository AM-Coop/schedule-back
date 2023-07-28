package ru.am.scheduleapp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.document.v2.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
        List<EventManager> managers = toEventManagerList(eventManagerSheet);
        List<Week> weeks = toWeekList(weekSheet);
        List<Room> rooms = toRoomList(roomsSheet);
        List<EventDocumentV2> events = toEventList(eventSheet, managers, locations, weeks);
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

    @SneakyThrows
    private List<EventManager> toEventManagerList(Sheet eventManagerSheet) {
        return eventManagerSheet.read().stream().filter(this::canOperate).skip(1).map(row -> {
            var num = (BigDecimal) row.getCell(0).getValue();
            var managerName = getRawOrNull(row, 1);
            var image = getRawOrNull(row, 2);
            var managerDescription = getRawOrNull(row, 3);
            var managerContact = getRawOrNull(row, 4);
            return new EventManager(
                    num.intValue(), managerName, image, managerDescription, managerContact
            );
        }).toList();
    }

    @SneakyThrows
    private List<Week> toWeekList(Sheet weekSheet) {
        return weekSheet.read().stream().filter(this::canOperate).skip(1).map(row -> {
            var num = (BigDecimal) row.getCell(0).getValue();
            var quote = getRawOrNull(row, 1);
            var notes = getRawOrNull(row, 2);
            var dateFrom = LocalDate.parse(getRawOrNull(row, 3));
            var dateTo = LocalDate.parse(getRawOrNull(row, 4));
            return new Week(
                    num.intValue(), quote, notes, dateFrom, dateTo
            );
        }).toList();
    }

    @SneakyThrows
    private List<Room> toRoomList(Sheet roomsSheet) {
        return roomsSheet.read().stream().filter(this::canOperate).skip(1).map(row -> {
            var id = (BigDecimal) row.getCell(0).getValue();
            var title = getRawOrNull(row, 1);
            var location = getRawOrNull(row, 2);
            return new Room(id.intValue(), title, location);
        }).toList();
    }

    @SneakyThrows
    private List<EventDocumentV2> toEventList(Sheet eventSheet, List<EventManager> managers,
                                              List<Location> locations, List<Week> weeks) {
        return eventSheet.read().stream().filter(this::canOperate).skip(1).map(row -> {
            var id = getRawOrNull(row, 0);
            var num = (BigDecimal) row.getCell(1).getValue();
            var title = getRawOrNull(row, 2);

            var locId = getRawOrNull(row, 3);
            var locIdNum = Integer.parseInt(locId.split("_")[1]);
            var location = locations.stream()
                    .filter(l -> l.getId() == locIdNum)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Location not found"));

            var date = LocalDate.parse(getRawOrNull(row, 4));
            var startTime = LocalTime.parse(getRawOrNull(row, 5));
            var endTime = LocalTime.parse(getRawOrNull(row, 6));
            var zoneId = ZoneId.of(getRawOrNull(row, 7));
            var description = getRawOrNull(row, 8);

            var managerId = getRawOrNull(row, 9);
            var managerIdNum = Integer.parseInt(managerId.split("_")[1]);
            var manager = managers.stream()
                    .filter(m -> m.getNum() == managerIdNum)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            var paid = Integer.parseInt(getRawOrNull(row, 10)) == 1;
            var paymentAmount = new BigDecimal(getRawOrNull(row, 11));
            var boldAm = Integer.parseInt(getRawOrNull(row, 12)) == 1;
            var boldUm = Integer.parseInt(getRawOrNull(row, 13)) == 1;
            var suitableUm = Integer.parseInt(getRawOrNull(row, 14)) == 1;
            var publish = Integer.parseInt(getRawOrNull(row, 15)) == 1;

            var weekId = getRawOrNull(row, 16);
            var weekIdNum = Integer.parseInt(weekId.split("_")[1]);
            Week week = weeks.stream()
                    .filter(w -> w.getNum() == weekIdNum)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Week not found"));

            return new EventDocumentV2(
                    id, num.intValue(), title, location, date, startTime, endTime, zoneId, description,
                    manager, paid, paymentAmount, boldAm, boldUm, suitableUm, publish, week
            );
        }).toList();
    }

    private String getRawOrNull(Row row, int i) {
        return Optional.ofNullable(row.getCell(i)).map(Cell::getRawValue).orElse(null);
    }

}
