package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.Role;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.stereotype.Repository;

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
}
