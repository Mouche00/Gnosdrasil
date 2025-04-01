package org.yc.gnosdrasil.gduserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yc.gnosdrasil.gduserservice.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
