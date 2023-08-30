package ru.am.scheduleapp.service.v2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.am.scheduleapp.model.wb.*;
import ru.am.scheduleapp.utils.SheetsMapperUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@NoArgsConstructor
public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

//    private Credential credential;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "/etc/am/google/creds.json";

    @Value("${ru.am.scheduleapp.google-creds-file-path}")
    public String credsPath;

    public <T extends WbObj> List<T> getSheet(String sheetName, Class<T> type) throws GeneralSecurityException, IOException {
        String to;

        if (type.isAssignableFrom(WbWeek.class)) {
            to = "G";
        } else if (type.isAssignableFrom(WbEvent.class)) {
            to = "Q";
        } else if (type.isAssignableFrom(WbEventManager.class)) {
            to = "F";
        } else if (type.isAssignableFrom(WbLocation.class)) {
            to = "H";
        } else throw new RuntimeException("no mapper for " + type.getName());

        final var transport = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1GmByqgnT1DzRKA5l46aVWgYANqpr0HijT5kqHczlDq0"; //TODO change
        final String range = sheetName + "!A2:" + to;
        Sheets service =
                new Sheets.Builder(transport, JSON_FACTORY, getCredentials(transport))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();

        if (type.isAssignableFrom(WbWeek.class)) {
            return (List<T>) SheetsMapperUtils.readWeekList(values);
        } else if (type.isAssignableFrom(WbEvent.class)) {
            return (List<T>) SheetsMapperUtils.readEventList(values);
        } else if (type.isAssignableFrom(WbEventManager.class)) {
            return (List<T>) SheetsMapperUtils.readEventManagerList(values);
        } else if (type.isAssignableFrom(WbLocation.class)) {
            return (List<T>) SheetsMapperUtils.readLocationList(values);
        } else throw new RuntimeException("no mapper for " + type.getName());
    }

    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.

//        InputStream in = new FileInputStream(credsPath);
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        InputStream in = new FileInputStream(credsPath);
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleCredential googleCredential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));

        return googleCredential;

//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("online")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new CustomAuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
