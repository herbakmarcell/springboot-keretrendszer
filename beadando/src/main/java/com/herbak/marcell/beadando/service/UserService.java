package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.*;
import com.herbak.marcell.beadando.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserDao userDao;
    private DrivingProfileDao drivingProfileDao;
    private CarDao carDao;
    private DrivingClassDao drivingClassDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, DrivingProfileDao drivingProfileDao, CarDao carDao, DrivingClassDao drivingClassDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.drivingProfileDao = drivingProfileDao;
        this.carDao = carDao;
        this.drivingClassDao = drivingClassDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(String username, String rawPassword, String email, String firstName, String lastName, Role role) {
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);

        userDao.add(user);

        if (user.getRole() == Role.STUDENT) {
            DrivingProfile drivingProfile = new DrivingProfile();
            drivingProfile.setStudent(user);
            drivingProfileDao.add(drivingProfile);
        }
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public String getUserFullName(String username) {
        User user = userDao.getByUsername(username);
        if (user == null) {
            return null;
        }
        return user.getFirstName() + " " + user.getLastName();
    }

    public DrivingProfile getDrivingProfile(String username) {
        User user = userDao.getByUsername(username);
        if (user == null) {
            return null;
        }
        return drivingProfileDao.getByUser(user);
    }

    public List<String> getAllTeachers() {
        List<User> teachers = userDao.getAllByRole(Role.TEACHER);
        return teachers.stream().map(User::getFullName).toList();
    }

    public void addCarToStudent(String student, String teacherFullName, String carName) {
        User user = userDao.getByUsername(student);
        DrivingProfile drivingProfile = drivingProfileDao.getByUser(user);
        Car car = carDao.getByNameAndOwner(carName, teacherFullName);
        drivingProfile.setCar(car);
        drivingProfileDao.update(drivingProfile);
    }

    public List<User> getAllStudentsByTeacher(String username) {
        List<DrivingProfile> drivingProfile = drivingProfileDao.getAll();
        return drivingProfile.stream()
                .filter(profile -> profile.getCar() != null)
                .filter(profile -> profile.getCar().getTeacher().getUsername().equals(username))
                .map(DrivingProfile::getStudent)
                .toList();
    }

    public List<User> getAllStudents() {
        return userDao.getAllByRole(Role.STUDENT);
    }

    public User getUserByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public int getHoursDriven(String username) {
        User user = userDao.getByUsername(username);
        List<DrivingClass> lessons = drivingClassDao.getAllByUser(user);

        int hoursDriven = 0;
        for (DrivingClass lesson : lessons) {
            hoursDriven += lesson.getHoursDriven();
        }

        return hoursDriven;
    }

    public int getDistanceDriven(String username) {
        User user = userDao.getByUsername(username);
        List<DrivingClass> lessons = drivingClassDao.getAllByUser(user);

        int distanceDriven = 0;
        for (DrivingClass lesson : lessons) {
            distanceDriven += lesson.getDistanceDriven();
        }

        return distanceDriven;
    }

}
