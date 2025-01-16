package com.herbak.marcell.beadando.entity;

import jakarta.persistence.*;

@Entity
@Table(name="driving_classes")
public class DrivingClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    private int hoursDriven;
    private int distanceDriven;

    private String driveDescription;

}
