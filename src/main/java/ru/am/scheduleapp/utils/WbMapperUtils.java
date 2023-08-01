package ru.am.scheduleapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import ru.am.scheduleapp.model.document.v2.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class WbMapperUtils {
    public static final String MSC_ZONE_STR = "+3";

    public static List<EventDocumentV2> readFromWb(ReadableWorkbook wb) throws IOException {
        var eventSheet = wb.getSheet(0).get();
        var weekSheet = wb.getSheet(1).get();
        var eventManagerSheet = wb.getSheet(2).get();
        var locationSheet = wb.getSheet(3).get();
        var roomsSheet = wb.getSheet(4).get();

        List<Location> locations = toLocationList(locationSheet);
        List<EventManager> managers = toEventManagerList(eventManagerSheet);
        List<Room> rooms = toRoomList(roomsSheet);
        List<EventDocumentV2> events = toEventList(eventSheet, managers, locations);
        List<WeekDocumentV2> weeks = toWeekList(weekSheet);

        //TODO остальные листы
        System.out.println(locations);

//        fillEventsForWeek(weeks.get(0), events)
        return List.of();
    }

    public static List<WeekDocumentV2> toWeekList(Sheet weekSheet) throws IOException {
        return weekSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var num = getNumWithDefault(row, 0);
            var quote = getRawOrNull(row, 1);
            var notes = getRawOrNull(row, 2);
            var dateFrom = getLocalDateOrNull(row, 3);
            var dateTo = getLocalDateOrNull(row, 4);
            return new WeekDocumentV2(
                    UUID.randomUUID().toString(), num.intValue(), quote, notes, dateFrom, dateTo, List.of()
            );
        }).toList();
    }

    public static List<Room> toRoomList(Sheet roomsSheet) throws IOException {
        return roomsSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var id = (BigDecimal) row.getCell(0).getValue();
            var title = getRawOrNull(row, 1);
            var location = getRawOrNull(row, 2);
            return new Room(id.intValue(), title, location);
        }).toList();
    }

    public static List<EventDocumentV2> toEventList(Sheet eventSheet, List<EventManager> managers,
                                                    List<Location> locations) throws IOException {
        return eventSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var num = getNumWithDefault(row, 0).intValue();
//            var num = getNumWithDefault(row, 1);
            var title = getRawOrNull(row, 1);

            var locName = getRawOrNull(row, 2);
//            var locIdNum = Integer.parseInt(locId.split("_")[1]);
            var location = locations.stream()
                    .filter(l -> Objects.equals(l.getName(), locName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Location not found: " + locName));

            var date = getLocalDateOrNull(row, 3);
            var startTime = getLocalTimeOrNull(row, 4);
            var endTime = getLocalTimeOrNull(row, 5);
            var zoneId = getZoneIdWithDefault(row, 6);

            var description = getRawOrNull(row, 7);

            var managerName = getRawOrNull(row, 8);
//            var managerIdNum = Integer.parseInt(managerId.split("_")[1]);
            var manager = managers.stream()
                    .filter(m -> Objects.equals(m.getManagerName(), managerName))
                    .findFirst()
                    .orElse(null);

            var paid = getBooleanWithDefault(row, 9);

            var paymentAmount = getNumWithDefault(row, 10);

            // TODO room with index 11

            var suitableUm = getBooleanWithDefault(row, 12);

            var boldAm = getBooleanWithDefault(row, 13);
            var boldUm = getBooleanWithDefault(row, 14);
            var publish = getBooleanWithDefault(row, 15);

            var weekId = getRawOrNull(row, 15); // TODO
//            System.out.println();
//            var weekIdNum = Integer.parseInt(weekId.split("_")[1]);
//            Week week = weeks.stream()
//                    .filter(w -> w.getNum() == weekIdNum)
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("Week not found"));

            return new EventDocumentV2(
                    UUID.randomUUID().toString(), num, title, location, date, startTime, endTime, zoneId, description,
                    manager, paid, paymentAmount, boldAm, boldUm, suitableUm, publish
            );
        }).toList();
    }

    public static List<EventManager> toEventManagerList(Sheet eventManagerSheet) throws IOException {
        return eventManagerSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var num = getNumWithDefault(row, 0).intValue();
            var managerName = getRawOrNull(row, 1);
            var image = getRawOrNull(row, 2);
            var managerDescription = getRawOrNull(row, 3);
            var managerContact = getRawOrNull(row, 4);
            return new EventManager(
                    num, managerName, image, managerDescription, managerContact
            );
        }).toList();
    }

    public static List<Location> toLocationList(Sheet locationSheet) throws IOException {
        return locationSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
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
