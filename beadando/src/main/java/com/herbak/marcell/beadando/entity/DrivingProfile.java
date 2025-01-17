package com.herbak.marcell.beadando.entity;

import jakarta.persistence.*;

@Entity
@Table(name="driving_profiles")
public class DrivingProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = true)
    private Car car = null;

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
