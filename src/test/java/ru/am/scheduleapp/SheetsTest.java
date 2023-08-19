package ru.am.scheduleapp;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.dhatim.fastexcel.reader.Sheet;
import org.junit.jupiter.api.Test;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbRoom;
import ru.am.scheduleapp.utils.WbMapperUtils;

import static ru.am.scheduleapp.utils.WbMapperUtils.*;

import java.io.*;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SheetsTest {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/etc/am/google/creds.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.

        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    @Test
    public void test() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"; //TODO change
        final String range = "Class Data!A2:E";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        System.out.println();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
        }
    }

    private List<Week> toWeekList(Sheet weekSheet) throws IOException {
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

    private List<WbEventManager> toEventManagerList(Sheet eventManagerSheet) throws IOException {
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

    private List<WbLocation> toLocationList(Sheet locationSheet) throws IOException {
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

    private List<WbRoom> toRoomList(Sheet roomsSheet) throws IOException {
        return roomsSheet.read().stream().filter(WbMapperUtils::canOperate).skip(1).map(row -> {
            var id = (BigDecimal) row.getCell(0).getValue();
            var title = getRawOrNull(row, 1);
            var location = getRawOrNull(row, 2);
            return new WbRoom(id.intValue(), title, location);
        }).toList();
    }

    private List<WbEvent> toEventList(Sheet eventsSheet) throws IOException {
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
