package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.am.scheduleapp.model.document.v2.Room;

@Repository
public interface RoomRepository extends CrudRepository<Room, String> {

}
