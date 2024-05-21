package com.sid.gl.usercontext.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "profil")
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profilSequenceGenerator")
    @SequenceGenerator(name = "profilSequenceGenerator", sequenceName = "profil_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "profile_name")
    private String profileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
