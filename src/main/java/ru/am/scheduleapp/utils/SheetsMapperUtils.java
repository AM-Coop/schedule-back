package ru.am.scheduleapp.utils;

import lombok.extern.slf4j.Slf4j;
import ru.am.scheduleapp.model.wb.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class SheetsMapperUtils {

    private static final String MSC_ZONE_STR = "+3";


    public static List<WbWeek> readWeekList(List<List<Object>> values) throws IOException {
        return values.stream().filter(SheetsMapperUtils::canOperate).map(row -> {
            var num = getNumWithDefault(row, 0);
            var quote = getRawOrNull(row, 1);
            var notes = getRawOrNull(row, 2);
            var dateFrom = getLocalDateOrNull(row, 3);
            var dateTo = getLocalDateOrNull(row, 4);
            return new WbWeek(
                    num.intValue(), quote, notes, dateFrom, dateTo
            );
        }).filter(row -> row.getNum() != -1).toList();
    }

    public static List<WbEventManager> readEventManagerList(List<List<Object>> values) throws IOException {
        return values.stream().filter(SheetsMapperUtils::canOperate).map(row -> {
            var id = new BigDecimal(row.get(0).toString()).intValue();
            var name = getRawOrNull(row, 1);
            var photo = getRawOrNull(row, 2);
            var description = getRawOrNull(row, 3);
            var contact = getRawOrNull(row, 4);
            return new WbEventManager(
                    id, name, photo, description, contact
            );
        }).filter(row -> row.getId() != -1).toList();
    }

    public static List<WbLocation> readLocationList(List<List<Object>> values) throws IOException {
        return values.stream().filter(SheetsMapperUtils::canOperate).map(row -> {
            var id = new BigDecimal(row.get(0).toString());
            var name = getRawOrNull(row, 1);
            var timeZone = getRawOrNull(row, 2);
            var address = getRawOrNull(row, 3);
            var rout = getRawOrNull(row, 4);
            var icon = getRawOrNull(row, 5);
            var description = getRawOrNull(row, 6);
            return new WbLocation(
                    id.intValue(), name, timeZone, address, rout, "region todo", icon, description
            );
        }).filter(row -> row.getNum() != -1).toList();
    }

    public static List<WbRoom> readRoomList(List<List<Object>> values) throws IOException {
        return values.stream().filter(SheetsMapperUtils::canOperate).map(row -> {
            var id = new BigDecimal(row.get(0).toString());
            var title = getRawOrNull(row, 1);
            var location = getRawOrNull(row, 2);
            return new WbRoom(id.intValue(), title, location);
        }).filter(row -> row.getNum() != -1).toList();
    }

    private static boolean canOperate(List<Object> row) {
        return !row.isEmpty() && row.get(0) != null;
    }

    public static List<WbEvent> readEventList(List<List<Object>> values) throws IOException {
        return values.stream().filter(row -> !row.isEmpty() && row.get(0) != null).map(row -> {
            var num = getNumWithDefault(row, 0).intValue();
            var title = getRawOrNull(row, 1);
            var locName = getRawOrNull(row, 2);
            var date = extractEventDate(row, 3);
            var startTime = getLocalTimeOrNull(row, 4);
            var endTime = getLocalTimeOrNull(row, 5);
            var zoneId = getZoneIdWithDefault(row, 6);
            var description = getRawOrNull(row, 7);
            var managerName = getRawOrNull(row, 8);
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
        }).filter(row -> row.getNum() != -1).toList();
    }

    private static boolean getBooleanWithDefault(List<Object> row, int i) {
        try {
            return row.get(i).toString().equals("TRUE");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    private static LocalTime getLocalTimeOrNull(List<Object> row, int i) {
        try {
            return LocalTime.parse(row.get(i).toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private static LocalDate getLocalDateOrNull(List<Object> row, int i) {
        try {
            return LocalDate.parse(row.get(i).toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private static LocalDate extractEventDate(List<Object> row, int i) {
        try {
            return LocalDate.parse(row.get(i).toString().split(" ")[1]);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private static String getRawOrNull(List<Object> row, int i) {
        try {
            return row.get(i).toString();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static BigDecimal getNumWithDefault(List<Object> row, int i) {
        try {
            return new BigDecimal(row.get(i).toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return new BigDecimal(-1);
    }

    public static ZoneId getZoneIdWithDefault(List<Object> row, int i) {
        var plus = "+";
        var zoneStr = row.get(i).toString();
        if (zoneStr == null) return ZoneId.of(MSC_ZONE_STR);
        if (zoneStr.contains(plus)) {
            var zone = plus + zoneStr.split(plus)[1];
            return ZoneId.of(zone);
        }
        return ZoneId.of(MSC_ZONE_STR);
    }
}
