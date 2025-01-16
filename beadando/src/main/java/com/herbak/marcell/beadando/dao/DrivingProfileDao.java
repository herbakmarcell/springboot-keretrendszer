package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingProfile;
import org.springframework.stereotype.Repository;

@Repository
public class DrivingProfileDao extends AbstractDao<DrivingProfile> {
    public DrivingProfileDao() {
        super(DrivingProfile.class);
    }
}
