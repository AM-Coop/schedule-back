package ru.am.scheduleapp.utils;

import ru.am.scheduleapp.model.dto.v2.EventResponseDto;
import ru.am.scheduleapp.model.dto.v2.LocationResponseDto;
import ru.am.scheduleapp.model.dto.v2.ManagerResponseDto;
import ru.am.scheduleapp.model.dto.v2.WeekResponseDto;
import ru.am.scheduleapp.model.entity.v2.Event;
import ru.am.scheduleapp.model.entity.v2.Location;
import ru.am.scheduleapp.model.entity.v2.Manager;
import ru.am.scheduleapp.model.entity.v2.Week;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapperUtils {

    public static WeekResponseDto mapFromWeekEntity(Week week) {
        return new WeekResponseDto(
                week.getId(),
                week.getNum(),
                week.getQuote(),
                week.getNotes(),
                week.getDateFrom(),
                week.getDateTo(),
                week.getQuoteForUm(),
                mapEvents(week.getEventList())
        );
    }

    private static List<EventResponseDto> mapEvents(List<Event> eventList) {
        return eventList.stream().map(e -> new EventResponseDto(
                e.getId(),
                e.getNum(),
                e.getTitle(),
                mapLocation(e.getLocation()),
                e.getDate(),
                e.getStartTime(),
                e.getEndTime(),
                e.getTimeZone(),
                e.getDescription(),
                mapManager(e.getManager()),
                e.isPaid(),
                e.getPaymentAmount(),
                e.isBoldAm(),
                e.isBoldUm(),
                e.isSuitableUm(),
                e.isPublish()
        )).collect(Collectors.toList());
    }

    private static ManagerResponseDto mapManager(Manager manager) {
        if (manager == null) return null;
        return new ManagerResponseDto(
                manager.getId(), manager.getNum(), manager.getName(), manager.getPhoto(), manager.getDescription(), manager.getContact(), manager.getWorldlyName()
        );
    }

    private static LocationResponseDto mapLocation(Location location) {
        if (location == null) return null;
        return new LocationResponseDto(
                location.getId(),
                location.getNum(),
                location.getName(),
                location.getRegion(),
                location.getTimeZone(),
                location.getAddress(),
                location.getRout(),
                location.getIcon(),
                location.getDescription()
        );
    }
}
