package com.sid.gl.usercontext.repository;

import com.sid.gl.usercontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findOneByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    User findByUsername(String username);


}
