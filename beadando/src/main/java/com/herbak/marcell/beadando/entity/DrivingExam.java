package com.herbak.marcell.beadando.entity;

import jakarta.persistence.*;

import java.time.DateTimeException;
import java.util.Date;

@Entity
@Table(name="driving_exams")
public class DrivingExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @Temporal(TemporalType.DATE)
    private Date examDate;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "path_id", nullable = false)
    private DrivingPath path;

    private int examErrorPoints;

    private boolean examResult;

}
