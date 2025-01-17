package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.Car;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarDao extends AbstractDao<Car> {
    public CarDao() {
        super(Car.class);
    }

    public List<Car> getAllByFullName(String fullName) {
        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];
        return entityManager.createQuery("SELECT c FROM Car c WHERE c.teacher.firstName = :firstName AND c.teacher.lastName = :lastName", Car.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList();
    }

    public Car getByNameAndOwner(String carName, String owner) {
        String firstName = owner.split(" ")[0];
        String lastName = owner.split(" ")[1];
        return entityManager.createQuery("SELECT c FROM Car c WHERE c.carName = :carName AND c.teacher.firstName = :firstName AND c.teacher.lastName = :lastName", Car.class)
                .setParameter("carName", carName)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult();
    }


}
