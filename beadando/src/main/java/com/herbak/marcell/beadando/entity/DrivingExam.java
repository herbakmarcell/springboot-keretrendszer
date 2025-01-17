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
    @JoinColumn(name = "supervisor_id", nullable = true)
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "path_id", nullable = true)
    private DrivingPath path;

    private int examErrorPoints = 0;

    @Column(nullable = true)
    private Boolean examResult = null;

    public Long getExamId() {
        return examId;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public DrivingPath getPath() {
        return path;
    }

    public void setPath(DrivingPath path) {
        this.path = path;
    }

    public int getExamErrorPoints() {
        return examErrorPoints;
    }

    public void setExamErrorPoints(int examErrorPoints) {
        this.examErrorPoints = examErrorPoints;
    }

    public Boolean getExamResult() {
        return examResult;
    }

    public void setExamResult(Boolean examResult) {
        this.examResult = examResult;
    }
}
