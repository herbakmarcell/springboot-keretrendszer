package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingClass;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrivingClassDao extends AbstractDao<DrivingClass> {
    public DrivingClassDao() {
        super(DrivingClass.class);
    }

    public List<DrivingClass> getAllByUser(User student) {
        return entityManager.createQuery("SELECT dc FROM DrivingClass dc WHERE dc.student= :student", DrivingClass.class)
                .setParameter("student", student)
                .getResultList();
    }
}
