package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingClass;
import org.springframework.stereotype.Repository;

@Repository
public class DrivingClassDao extends AbstractDao<DrivingClass> {
    public DrivingClassDao() {
        super(DrivingClass.class);
    }
}
