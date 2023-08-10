package ru.am.scheduleapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbRoom;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class WbMapperUtils {
    public static final String MSC_ZONE_STR = "+3";

    public static boolean isActualWeek(Week week) {
        var now = LocalDate.now();
        return week.getDateTo().isAfter(now) || week.getDateFrom().isEqual(now);
    }

    public static List<Week> readWeekList(ReadableWorkbook wb) throws IOException {
        var weekSheet = wb.getSheet(1).get();
        return weekSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var num = getNumWithDefault(row, 0);
            var quote = getRawOrNull(row, 1);
            var notes = getRawOrNull(row, 2);
            var dateFrom = getLocalDateOrNull(row, 3);
            var dateTo = getLocalDateOrNull(row, 4);
            return new Week(
                    UUID.randomUUID(), num.intValue(), quote, notes, dateFrom, dateTo, List.of()
            );
        }).toList();
    }

    public static List<WbRoom> readRoomList(ReadableWorkbook wb) throws IOException {
        var roomsSheet = wb.getSheet(4).get();
        return roomsSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var id = (BigDecimal) row.getCell(0).getValue();
            var title = getRawOrNull(row, 1);
            var location = getRawOrNull(row, 2);
            return new WbRoom(id.intValue(), title, location);
        }).toList();
    }

    public static List<WbEvent> readEventList(ReadableWorkbook wb) throws IOException {
        var eventSheet = wb.getSheet(0).get();

        return eventSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var num = getNumWithDefault(row, 0).intValue();
//            var num = getNumWithDefault(row, 1);
            var title = getRawOrNull(row, 1);

            var locName = getRawOrNull(row, 2);


            var date = getLocalDateOrNull(row, 3);
            var startTime = getLocalTimeOrNull(row, 4);
            var endTime = getLocalTimeOrNull(row, 5);
            var zoneId = getZoneIdWithDefault(row, 6);

            var description = getRawOrNull(row, 7);

            var managerName = getRawOrNull(row, 8);
//            var managerIdNum = Integer.parseInt(managerId.split("_")[1]);


            var paid = getBooleanWithDefault(row, 9);

            var paymentAmount = getNumWithDefault(row, 10);

            // TODO room with index 11

            var suitableUm = getBooleanWithDefault(row, 12);

            var boldAm = getBooleanWithDefault(row, 13);
            var boldUm = getBooleanWithDefault(row, 14);
            var publish = getBooleanWithDefault(row, 15);

            var weekId = getRawOrNull(row, 15); // TODO

            return new WbEvent(
                    num, title, locName, date, startTime, endTime, zoneId, description,
                    managerName, paid, paymentAmount, boldAm, boldUm, suitableUm, publish
            );
        }).toList();
    }

    public static List<WbEventManager> readEventManagerList(ReadableWorkbook wb) throws IOException {
        var eventManagerSheet = wb.getSheet(2).get();
        return eventManagerSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var id = getNumWithDefault(row, 0).intValue();
            var name = getRawOrNull(row, 1);
            var photo = getRawOrNull(row, 2);
            var description = getRawOrNull(row, 3);
            var contact = getRawOrNull(row, 4);
            return new WbEventManager(
                    id, name, photo, description, contact
            );
        }).toList();
    }

    public static List<WbLocation> readLocationList(ReadableWorkbook wb) throws IOException {
        var locationSheet = wb.getSheet(3).get();
        return locationSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
                    var id = (BigDecimal) row.getCell(0).getValue();
                    var name = getRawOrNull(row, 1);
                    var timeZone = getRawOrNull(row, 2);
                    var address = getRawOrNull(row, 3);
                    var rout = getRawOrNull(row, 4);
                    var icon = getRawOrNull(row, 5);
                    var description = getRawOrNull(row, 6);
                    return new WbLocation(
                            id.intValue(), name, timeZone, address, rout, "region todo", icon, description
                    );
                }
        ).toList();
    }

    public static Boolean getBooleanWithDefault(Row row, int i) {
        return Optional.ofNullable(row.getCell(i)).map(Cell::asBoolean).orElse(false);
    }

    public static ZoneId getZoneIdWithDefault(Row row, int i) {
        var plus = "+";
        var zoneStr = getRawOrNull(row, i);
        if (zoneStr == null) return ZoneId.of(MSC_ZONE_STR);
        if (zoneStr.contains(plus)) {
            var zone = plus + zoneStr.split(plus)[1];
            return ZoneId.of(zone);
        }
        return ZoneId.of(MSC_ZONE_STR);
    }

    public static String getRawOrNull(Row row, int i) {
        try {
            return Optional.ofNullable(row.getCell(i)).map(Cell::getRawValue).orElse(null);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static BigDecimal getNumWithDefault(Row row, int i) {
        try {
            return new BigDecimal(getRawOrNull(row, i));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return new BigDecimal(-1);
    }

    public static LocalDateTime getLocalDateTimeOrNull(Row row, int i) {
        try {
            return row.getCell(i).asDate();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static LocalDate getLocalDateOrNull(Row row, int i) {
        return Optional.ofNullable(getLocalDateTimeOrNull(row, i)).map(LocalDateTime::toLocalDate).orElse(null);
    }

    public static LocalTime getLocalTimeOrNull(Row row, int i) {
        return Optional.ofNullable(getLocalDateTimeOrNull(row, i)).map(LocalDateTime::toLocalTime).orElse(null);
    }

    public static boolean canOperate(Row row) {
        boolean res = false;
        for (int i = 0; i < row.getCellCount(); i++) {
            String rawValue = Optional.ofNullable(row.getCell(i)).map(Cell::getRawValue).orElse(null);
            Object value = Optional.ofNullable(row.getCell(i)).map(Cell::getValue).orElse(null);
            var bool = true;
            if (value instanceof Boolean) {
                bool = (Boolean) value;
            }
            res = res || (rawValue != null && !rawValue.isEmpty() && bool);
        }
        return res;
    }
}
