package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.CarDao;
import com.herbak.marcell.beadando.dao.UserDao;
import com.herbak.marcell.beadando.entity.Car;
import com.herbak.marcell.beadando.entity.CarType;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private CarDao carDao;
    private UserDao userDao;

    @Autowired
    public CarService(CarDao carDao, UserDao userDao) {
        this.carDao = carDao;
        this.userDao = userDao;
    }

    public List<String> getCarsByFullName(String fullName) {
        List<Car> cars = carDao.getAllByFullName(fullName);
        return cars.stream().map(Car::getCarName).toList();
    }

    public void addCar(CarType carType, String carName, String licensePlate, String teacher) {
        User currTeacher = userDao.getByUsername(teacher);
        Car car = new Car();
        car.setCarType(carType);
        car.setCarName(carName);
        car.setLicensePlate(licensePlate);
        car.setTeacher(currTeacher);
        carDao.add(car);
    }
}
