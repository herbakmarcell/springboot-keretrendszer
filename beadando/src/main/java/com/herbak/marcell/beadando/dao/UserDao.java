package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingProfile;
import com.herbak.marcell.beadando.entity.Role;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends AbstractDao<User> {
    public UserDao() {
        super(User.class);
    }

    public User getByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public List<User> getAllByRole(Role role) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", role)
                .getResultList();
    }
    public User getByFullName(String fullName){
        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];
        return entityManager.createQuery("SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName", User.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult();
    }
}
