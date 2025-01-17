package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingPath;
import org.springframework.stereotype.Repository;

@Repository
public class DrivingPathDao extends AbstractDao<DrivingPath> {
    public DrivingPathDao() {
        super(DrivingPath.class);
    }

    public DrivingPath getByName(String pathName) {
        return entityManager.createQuery("SELECT dp FROM DrivingPath dp WHERE dp.pathName = :pathName", DrivingPath.class)
                .setParameter("pathName", pathName)
                .getSingleResult();
    }
}
