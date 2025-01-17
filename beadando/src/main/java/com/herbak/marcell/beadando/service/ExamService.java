package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.DrivingExamDao;
import com.herbak.marcell.beadando.dao.DrivingPathDao;
import com.herbak.marcell.beadando.entity.DrivingExam;
import com.herbak.marcell.beadando.entity.DrivingPath;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ExamService {
    private final UserService userService;
    DrivingExamDao drivingExamDao;
    DrivingPathDao drivingPathDao;

    @Autowired
    public ExamService(DrivingExamDao drivingExamDao, DrivingPathDao drivingPathDao, UserService userService) {
        this.drivingExamDao = drivingExamDao;
        this.drivingPathDao = drivingPathDao;
        this.userService = userService;
    }

    public void addExam(LocalDate date, User student, User teacher) {
        DrivingExam exam = new DrivingExam();
        exam.setExamDate(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        exam.setStudent(student);
        exam.setTeacher(teacher);

        drivingExamDao.add(exam);
    }

    public List<DrivingExam> getEmptyExams() {
        return drivingExamDao.getEmptyExams();
    }

    public void addSupervisorToExam(String id, User supervisor) {
        DrivingExam exam = drivingExamDao.get(Integer.parseInt(id));
        exam.setSupervisor(supervisor);
        drivingExamDao.update(exam);
    }

    public void addResultToExam(String id, String pathName, int errorPoints, boolean result) {
        DrivingExam exam = drivingExamDao.get(Integer.parseInt(id));
        DrivingPath path = drivingPathDao.getByName(pathName);
        exam.setPath(path);
        exam.setExamErrorPoints(errorPoints);
        exam.setExamResult(result);
        drivingExamDao.update(exam);
    }

    public List<DrivingExam> getAllUnratedExamsBySupervisor(String supervisorUsername) {
        User supervisor = userService.getUserByUsername(supervisorUsername);
        return drivingExamDao.getUnratedExamsBySupervisor(supervisor);
    }

    public List<DrivingPath> getDrivingPaths() {
        return drivingPathDao.getAll();
    }

    public DrivingExam getExamById(int id) {
        return drivingExamDao.get(id);
    }

    public boolean isThereUpcomingExam(String username) {
        User user = userService.getUserByUsername(username);
        return drivingExamDao.isThereUpcomingExam(user);
    }

    public DrivingExam getUpcomingExam(String username) {
        User user = userService.getUserByUsername(username);
        return drivingExamDao.getUpcomingExam(user);
    }
}
