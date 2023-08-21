package ru.am.scheduleapp;

import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbWeek;
import ru.am.scheduleapp.service.v2.GoogleSheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheetsTest {
    private final GoogleSheetsService service = new GoogleSheetsService();

    @Test
    public void test() throws GeneralSecurityException, IOException {
        List<WbEvent> event = service.getSheet("event", WbEvent.class);
        System.out.println(event);

        List<WbEventManager> managers = service.getSheet("manager", WbEventManager.class);
        System.out.println(managers);

        List<WbLocation> locations = service.getSheet("location", WbLocation.class);
        System.out.println(locations);

        List<WbWeek> weeks = service.getSheet("week", WbWeek.class);
        System.out.println(weeks);
    }
}
