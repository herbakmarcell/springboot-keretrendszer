package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.DrivingClassDao;
import com.herbak.marcell.beadando.entity.DrivingClass;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    DrivingClassDao drivingClassDao;

    @Autowired
    public LessonService(DrivingClassDao drivingClassDao) {
        this.drivingClassDao = drivingClassDao;
    }

    public void addLesson(User student, User teacher, int hours, int distance, String description) {
        DrivingClass drivingClass = new DrivingClass();
        drivingClass.setStudent(student);
        drivingClass.setTeacher(teacher);
        drivingClass.setHoursDriven(hours);
        drivingClass.setDistanceDriven(distance);
        drivingClass.setDriveDescription(description);
        drivingClassDao.add(drivingClass);
    }

}
