package ru.am.scheduleapp.service.security;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.am.scheduleapp.model.dto.security.CustomUsrDetails;
import ru.am.scheduleapp.model.entity.security.Role;
import ru.am.scheduleapp.model.entity.security.User;
import ru.am.scheduleapp.repository.security.RoleRepository;
import ru.am.scheduleapp.repository.security.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class CustomUsrDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            Role usr = new Role();
            usr.setRoleName("usr");
            Role adm = new Role();
            usr.setRoleName("adm");
            roles = roleRepository.saveAll(List.of(
                    usr, adm
            ));
        }

        Role usr = roles.stream().filter(r -> Objects.equals(r.getRoleName(), "usr")).findFirst().get();
        Role adm = roles.stream().filter(r -> Objects.equals(r.getRoleName(), "adm")).findFirst().get();


        userRepo.findByEmail("user").orElseGet(() -> {
            User user = new User();
            user.setEmail("user");
            user.setPassword("$2a$12$866ROzZwHMIkjDG7wKp/H.roeYGDJMUhdxqKiyt7cDu8h7XuN/76q");
            user.setRoles(Set.of(usr));
            return userRepo.save(user);
        });
        userRepo.findByEmail("admin").orElseGet(() -> {
            User user = new User();
            user.setEmail("admin");
            user.setPassword("$2a$12$866ROzZwHMIkjDG7wKp/H.roeYGDJMUhdxqKiyt7cDu8h7XuN/76q");
            user.setRoles(Set.of(adm));
            return userRepo.save(user);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email = " + email + " not exist!"));
        return new CustomUsrDetails(user);
    }
}
