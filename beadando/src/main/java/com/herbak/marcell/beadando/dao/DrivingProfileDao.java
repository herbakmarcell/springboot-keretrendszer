package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingProfile;
import com.herbak.marcell.beadando.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class DrivingProfileDao extends AbstractDao<DrivingProfile> {
    public DrivingProfileDao() {
        super(DrivingProfile.class);
    }

    public DrivingProfile getByUser(User user) {
        return entityManager.createQuery("SELECT dp FROM DrivingProfile dp WHERE dp.student = :user", DrivingProfile.class)
                .setParameter("user", user)
                .getSingleResult();
    }
}
