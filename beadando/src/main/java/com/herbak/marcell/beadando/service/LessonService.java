package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.DrivingClassDao;
import com.herbak.marcell.beadando.dao.UserDao;
import com.herbak.marcell.beadando.entity.DrivingClass;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    DrivingClassDao drivingClassDao;
    UserDao userDao;

    @Autowired
    public LessonService(DrivingClassDao drivingClassDao, UserDao userDao) {
        this.drivingClassDao = drivingClassDao;
        this.userDao = userDao;
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

    public List<DrivingClass> getAllByTeacher(String teacherUsername) {
        User teacher = userDao.getByUsername(teacherUsername);

        return drivingClassDao.getAllByTeacher(teacher);
    }

    public void deleteLesson(String id) {
        drivingClassDao.deleteById(Integer.parseInt(id));
    }

    public DrivingClass getLessonById(String id) {
        return drivingClassDao.get(Integer.parseInt(id));
    }
}
