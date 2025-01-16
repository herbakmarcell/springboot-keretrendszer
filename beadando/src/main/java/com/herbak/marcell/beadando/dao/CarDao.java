package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.Car;
import org.springframework.stereotype.Repository;

@Repository
public class CarDao extends AbstractDao<Car> {
    public CarDao() {
        super(Car.class);
    }
}
