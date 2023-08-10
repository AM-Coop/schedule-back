package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.am.scheduleapp.model.entity.v2.Manager;

import java.util.Optional;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, String> {

    Optional<Manager> findByNum(int num);


    Optional<Manager> findByName(String name);


}
