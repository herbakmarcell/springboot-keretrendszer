package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.AbstractDao;
import com.herbak.marcell.beadando.dao.Dao;
import com.herbak.marcell.beadando.dao.DrivingProfileDao;
import com.herbak.marcell.beadando.dao.UserDao;
import com.herbak.marcell.beadando.entity.DrivingProfile;
import com.herbak.marcell.beadando.entity.Role;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private Dao<User> userDao;
    private Dao<DrivingProfile> drivingProfileDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, DrivingProfileDao drivingProfileDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.drivingProfileDao = drivingProfileDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(String username, String rawPassword, String email, Role role) {
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
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


}
