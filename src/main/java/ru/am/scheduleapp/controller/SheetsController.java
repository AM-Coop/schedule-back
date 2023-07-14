package ru.am.scheduleapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.am.scheduleapp.service.SheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@AllArgsConstructor
public class SheetsController {
    private static final String YOUR_SPREADSHEET_ID = "12Gbgvut6OicOSLVVZYotg9wgSyz0YYqyXJSJY9ZSe2k";
    private static final String RANGE = "Sheet1";

    private final SheetsService sheetsService;

    @GetMapping("/")
    public List<List<Object>> getDataFromSheet() {
        try {
            return sheetsService.readDataFromSheet(YOUR_SPREADSHEET_ID, RANGE);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}