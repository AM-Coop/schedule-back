package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.am.scheduleapp.model.entity.v2.WeekInfo;

@Repository
public interface WeekInfoRepository extends CrudRepository<WeekInfo, String> {


}
