package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingExam;
import org.springframework.stereotype.Repository;

@Repository
public class DrivingExamDao extends AbstractDao<DrivingExam> {
    public DrivingExamDao() {
        super(DrivingExam.class);
    }
}
