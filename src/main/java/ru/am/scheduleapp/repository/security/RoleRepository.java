package ru.am.scheduleapp.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.am.scheduleapp.model.entity.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
//    Optional<Role> findByEmail(String email);
}
