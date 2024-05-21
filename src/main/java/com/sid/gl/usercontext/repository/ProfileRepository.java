package com.sid.gl.usercontext.repository;

import com.sid.gl.usercontext.domain.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profil,Long> {
    Profil findByProfileName(String profileName);

}
