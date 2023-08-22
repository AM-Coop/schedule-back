package ru.am.scheduleapp.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.am.scheduleapp.model.entity.v2.Event;
import ru.am.scheduleapp.model.entity.v2.Location;
import ru.am.scheduleapp.model.entity.v2.Manager;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbWeek;
import ru.am.scheduleapp.repository.v2.EventRepository;
import ru.am.scheduleapp.repository.v2.LocationRepository;
import ru.am.scheduleapp.repository.v2.ManagerRepository;
import ru.am.scheduleapp.repository.v2.WeekRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetsService {
    private final EventRepository eventRepository;
    private final WeekRepository weekRepository;

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

//            managerRepository.findByName(ev)
            if (entity.getManager() == null) {
                managerRepository.findByName(event.getManagerName()).ifPresent(entity::setManager);
            }

            if (entity.getLocation() == null) {
                locationRepository.findByName(event.getLocationName()).ifPresent(entity::setLocation);
            }

            if (entity.getWeek() == null) {
                weekRepository.findByEventDate(event.getDate()).ifPresent(entity::setWeek);
            }


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
            entity.setNotes(week.getNotes());
            entity.setQuote(week.getQuote());
            entity.setDateFrom(week.getDateFrom());
            entity.setDateTo(week.getDateTo());
            entity.setQuoteForUm(week.getQuoteForUm());
            weekRepository.save(entity);

//            List<Event> events = eventRepository.findAllByDateBetween(week.getDateFrom(), week.getDateTo());
//            week.getEventList().clear();
//            week.getEventList().addAll(events);
        });
    }

    public void refreshFromGoogleSheets() throws GeneralSecurityException, IOException {
        List<WbEvent> event = googleSheetsService.getSheet("event", WbEvent.class);

        List<WbEventManager> managers = googleSheetsService.getSheet("manager", WbEventManager.class);

        List<WbLocation> locations = googleSheetsService.getSheet("location", WbLocation.class);

        List<WbWeek> weeks = googleSheetsService.getSheet("week", WbWeek.class);

//        updateRooms(WbMapperUtils.readRoomList(wb)); // TODO
        updateLocations(locations);
        updateManagers(managers);
        updateWeeksFromWb(weeks);
        updateEvents(event);
    }


}
