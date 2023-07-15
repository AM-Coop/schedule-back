package ru.am.scheduleapp.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetsService {

    private Sheets sheets;



    @PostConstruct
    public void init() {
        System.out.println();
    }


    @SneakyThrows
    public List<List<Object>> readDataFromSheet(String spreadsheetId, String range) {

        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        return response.getValues();
    }
}
