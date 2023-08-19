package ru.am.scheduleapp.utils;

import org.dhatim.fastexcel.reader.Sheet;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbRoom;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class SheetsMapperUtils extends WbMapperUtils {

    public static List<Week> toWeekList(Sheet weekSheet) throws IOException {
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

    public static List<WbEventManager> toEventManagerList(Sheet eventManagerSheet) throws IOException {
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

    public static List<WbLocation> toLocationList(Sheet locationSheet) throws IOException {
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
        }).toList();
    }

    public static List<WbRoom> toRoomList(Sheet roomsSheet) throws IOException {
        return roomsSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var id = (BigDecimal) row.getCell(0).getValue();
            var title = getRawOrNull(row, 1);
            var location = getRawOrNull(row, 2);
            return new WbRoom(id.intValue(), title, location);
        }).toList();
    }

    public static List<WbEvent> toEventList(Sheet eventsSheet) throws IOException {
        return eventsSheet.read().stream().filter(WbMapperUtils::canOperate).map(row -> {
            var num = getNumWithDefault(row, 0).intValue();
            var title = getRawOrNull(row, 1);
            var locName = getRawOrNull(row, 2);
            var date = getLocalDateOrNull(row, 3);
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
        }).toList();
    }
}
