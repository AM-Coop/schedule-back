package ru.am.scheduleapp.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.am.scheduleapp.configuration.properties.ScheduleAppProps;
import ru.am.scheduleapp.model.entity.v2.Event;
import ru.am.scheduleapp.model.entity.v2.Location;
import ru.am.scheduleapp.model.entity.v2.Manager;
import ru.am.scheduleapp.model.entity.v2.Week;
import ru.am.scheduleapp.model.wb.WbEvent;
import ru.am.scheduleapp.model.wb.WbEventManager;
import ru.am.scheduleapp.model.wb.WbLocation;
import ru.am.scheduleapp.model.wb.WbRoom;
import ru.am.scheduleapp.repository.v2.*;
import ru.am.scheduleapp.utils.WbMapperUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetsService {
    private final EventRepository eventRepository;
    private final WeekRepository weekRepository;

    private final LocationRepository locationRepository;

    private final ManagerRepository managerRepository;

    private final RoomRepository roomRepository;

    private final ScheduleAppProps scheduleAppProps;

//    private boolean useDateFilter;

//    @PostConstruct
//    public void init() {
//        useDateFilter = Boolean.getBoolean(useDateFilterProp);
//    }

    public void operateNew(MultipartFile file) throws IOException {
        log.info("new file {}", file.getName());
        var path = saveFile(file);
        try (FileInputStream fis = new FileInputStream(path);
             ReadableWorkbook wb = new ReadableWorkbook(fis)) {
            operateWb(wb);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private String saveFile(MultipartFile f) throws IOException {
        String pathname = scheduleAppProps.getWbDirLocation() + f.getOriginalFilename();
        File dest = new File(pathname);
        f.transferTo(dest);
        return pathname;
    }

    private void operateWb(ReadableWorkbook wb) throws IOException {
//        Pair<List<Event>, List<Week>> tuple = WbMapperUtils.readFromWb(wb);
//        updateEvents(tuple.getFirst());
//        updateWeeks(tuple.getSecond());
        updateRooms(WbMapperUtils.readRoomList(wb)); // TODO
        updateLocations(WbMapperUtils.readLocationList(wb));
        updateManagers(WbMapperUtils.readEventManagerList(wb));
        updateWeeks(WbMapperUtils.readWeekList(wb));

        updateEvents(WbMapperUtils.readEventList(wb));

    }

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

            managerRepository.save(entity);
        });
    }

    private void updateRooms(List<WbRoom> wbRooms) {
        wbRooms.forEach(room -> {
            // todo
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
    public void updateWeeks(List<Week> weeks) {
        weeks.forEach(week -> {
            Week entity = weekRepository.findWeekByDateFromAndDateTo(week.getDateFrom(), week.getDateTo()).orElseGet(() -> {
                log.info("new week {}", week);
                return new Week();
            });
            entity.setNotes(week.getNotes());
            entity.setQuote(week.getQuote());
            entity.setDateFrom(week.getDateFrom());
            entity.setDateTo(week.getDateTo());
            weekRepository.save(entity);

//            List<Event> events = eventRepository.findAllByDateBetween(week.getDateFrom(), week.getDateTo());
//            week.getEventList().clear();
//            week.getEventList().addAll(events);
        });
    }


}
