package ru.am.scheduleapp.utils;

import ru.am.scheduleapp.model.dto.v2.EventResponseDto;
import ru.am.scheduleapp.model.dto.v2.LocationResponseDto;
import ru.am.scheduleapp.model.dto.v2.ManagerResponseDto;
import ru.am.scheduleapp.model.dto.v2.WeekResponseDto;
import ru.am.scheduleapp.model.entity.v2.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DtoMapperUtils {

    public static WeekResponseDto mapFromWeekEntity(Week week, boolean showAm, boolean showUm) {
        WeekInfo weekInfo = week.getWeekInfos().stream().filter(info -> {
            if (showAm) return Objects.equals(info.getCommunity(), "AM");
            else return Objects.equals(info.getCommunity(), "UM");
        }).findFirst().get();
        return new WeekResponseDto(
                week.getId(),
                week.getNum(),
                week.getDateFrom(),
                week.getDateTo(),
                weekInfo.getQuote(),
                weekInfo.getNote1(),
                weekInfo.getNote2(),
                weekInfo.getCommunity(),
                mapEvents(week.getEventList(), showAm, showUm)
        );
    }


    private static List<EventResponseDto> mapEvents(List<Event> eventList, boolean showAm, boolean showUm) {
        return eventList.stream()
                .filter(elem -> {
                    if (!elem.isPublish()) return false;

                    if (showAm && showUm) return true;
                    else if (showAm) return elem.isSuitableAm();
                    else return elem.isSuitableUm();
                })
                .map(e -> {
                    var isBold = false;
                    if (showAm) isBold = e.isBoldAm();
                    else if (showUm) isBold = e.isBoldUm();
                    return new EventResponseDto(
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
                            e.isSuitableAm(),
                            isBold,
                            e.isSuitableUm(),
                            e.isPublish()
                    );
                }).collect(Collectors.toList());
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
                location.getDescription(),
                location.getDisplayOrder()
        );
    }
}
