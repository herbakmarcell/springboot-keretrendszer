package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingPath;
import org.springframework.stereotype.Repository;

@Repository
public class DrivingPathDao extends AbstractDao<DrivingPath> {
    public DrivingPathDao() {
        super(DrivingPath.class);
    }
}
