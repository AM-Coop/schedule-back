package ru.am.scheduleapp.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.am.scheduleapp.model.entity.v2.*;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbWeek;
import ru.am.scheduleapp.repository.v2.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetsService {
    private final EventRepository eventRepository;
    private final WeekRepository weekRepository;

    private final WeekInfoRepository weekInfoRepository;

    private final LocationRepository locationRepository;

    private final ManagerRepository managerRepository;

    private final GoogleSheetsService googleSheetsService;


    @Transactional
    public void updateManagers(List<WbEventManager> wbEventManagers) {
        wbEventManagers.forEach(manager -> {
            Manager entity = managerRepository.findByNum(manager.getId()).orElseGet(() -> {
                log.info("creating new manager {}", manager);
                return new Manager();
            });
            entity.setName(manager.getName());
            entity.setContact(manager.getContact());
            entity.setDescription(manager.getDescription());
            entity.setPhoto(manager.getPhoto());
            entity.setNum(manager.getId());
            entity.setWorldlyName(manager.getWorldlyName());

            managerRepository.save(entity);
        });
    }

    @Transactional
    public void updateLocations(List<WbLocation> wbLocations) {
        wbLocations.forEach(location -> {
            Location entity = locationRepository.findByNum(location.getNum()).orElseGet(() -> {
                log.info("creating new location {}", location);
                return new Location();
            });
            entity.setNum(location.getNum());
            entity.setName(location.getName());
            entity.setIcon(location.getIcon());
            entity.setRout(location.getRout());
            entity.setRegion(location.getRegion());
            entity.setAddress(location.getAddress());
            entity.setTimeZone(location.getTimeZone());
            entity.setDescription(location.getDescription());
            entity.setDisplayOrder(location.getOrder());
            locationRepository.save(entity);
        });
    }

    @Transactional
    public void updateEvents(List<WbEvent> events) {
        events.forEach(event -> {
            Event entity = eventRepository.findByNum(event.getNum()).orElseGet(() -> {
                log.info("new event: {}", event);
                return new Event();
            });
            entity.setNum(event.getNum());
            entity.setDate(event.getDate());
            entity.setDescription(event.getDescription());
            entity.setPaid(event.isPaid());
            entity.setTimeZone(event.getTimeZone());
            entity.setBoldAm(event.isBoldAm());
            entity.setEndTime(event.getEndTime());
            entity.setPaymentAmount(event.getPaymentAmount());
            entity.setPublish(event.isPublish());
            entity.setStartTime(event.getStartTime());
            entity.setSuitableUm(event.isSuitableUm());
            entity.setTitle(event.getTitle());
            entity.setBoldUm(event.isBoldUm());
            entity.setSuitableAm(event.isSuitableAm());

//            managerRepository.findByName(ev)
//            if (entity.getManager() == null) {
            managerRepository.findByName(event.getManagerName()).ifPresent(entity::setManager);
//            }

//            if (entity.getLocation() == null) {
            locationRepository.findByName(event.getLocationName()).ifPresent(entity::setLocation);
//            }

//            if (entity.getWeeks() == null ||) {
            Week byEventDate = weekRepository.findByEventDate(event.getDate()).get();
            entity.setWeek(byEventDate);
//            }


            eventRepository.save(entity);

        });
    }


    @Transactional
    public void updateWeeksFromWb(List<WbWeek> weeks) {
        weeks.forEach(week -> {
            Week entity = weekRepository.findWeekByDateFromAndDateTo(week.getDateFrom(), week.getDateTo()).orElseGet(() -> {
                log.info("new week {}", week);
                return new Week();
            });
            entity.setNum(week.getNum());
            entity.setDateFrom(week.getDateFrom());
            entity.setDateTo(week.getDateTo());

            weekRepository.save(entity);

        });
    }

    @Transactional
    public void updateWeekInfosFromWb(List<WbWeek> weeks) {
        weeks.forEach(week -> {
            Week entity = weekRepository.findWeekByDateFromAndDateTo(week.getDateFrom(), week.getDateTo()).get();
            WeekInfo weekInfo;
            if (entity.getWeekInfos().isEmpty()) {
                weekInfo = new WeekInfo();
                weekInfo.setCommunity(week.getCommunity());
                weekInfo.setNote1(week.getNote1());
                weekInfo.setNote2(week.getNote2());
                weekInfo.setQuote(week.getQuote());


            } else {
                weekInfo = entity.getWeekInfos().stream()
                        .filter(info -> Objects.equals(info.getCommunity(), week.getCommunity())).findFirst().orElse(null);
                if (weekInfo != null) {
                    weekInfo.setNote1(week.getNote1());
                    weekInfo.setNote2(week.getNote2());
                    weekInfo.setQuote(week.getQuote());
                    weekInfo.setCommunity(week.getCommunity());

                } else {
                    weekInfo = new WeekInfo();
                    weekInfo.setCommunity(week.getCommunity());
                    weekInfo.setNote1(week.getNote1());
                    weekInfo.setNote2(week.getNote2());
                    weekInfo.setQuote(week.getQuote());
                    weekInfo.setWeek(entity);

                }
            }
            weekInfo.setWeek(entity);
            weekInfoRepository.save(weekInfo);

        });
    }

    public void refreshFromGoogleSheets(boolean refreshEntities) throws GeneralSecurityException, IOException {
        log.info("refreshFromGoogleSheets");
        List<WbEvent> event = googleSheetsService.getSheet("event", WbEvent.class);

        List<WbEventManager> managers = googleSheetsService.getSheet("manager", WbEventManager.class);

        List<WbLocation> locations = googleSheetsService.getSheet("location", WbLocation.class);

        List<WbWeek> weeks = googleSheetsService.getSheet("week", WbWeek.class);

//        updateRooms(WbMapperUtils.readRoomList(wb)); // TODO
        if (refreshEntities) {
            log.info("refreshEntities");
            updateLocations(locations);
            updateManagers(managers);
            updateWeeksFromWb(weeks);
            updateEvents(event);
            updateWeekInfosFromWb(weeks);
        }

    }


}
