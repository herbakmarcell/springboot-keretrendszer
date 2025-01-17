package com.herbak.marcell.beadando.dao;

import com.herbak.marcell.beadando.entity.DrivingExam;
import com.herbak.marcell.beadando.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrivingExamDao extends AbstractDao<DrivingExam> {
    public DrivingExamDao() {
        super(DrivingExam.class);
    }

    public List<DrivingExam> getEmptyExams(){
        return entityManager.createQuery("SELECT d FROM DrivingExam d WHERE d.supervisor IS NULL", DrivingExam.class)
                .getResultList();
    }

    public List<DrivingExam> getUnratedExamsBySupervisor(User supervisor){
        return entityManager.createQuery("SELECT d FROM DrivingExam d WHERE d.supervisor = :supervisor AND d.examResult IS NULL", DrivingExam.class)
                .setParameter("supervisor", supervisor)
                .getResultList();
    }

    public boolean isThereUpcomingExam(User student){
        return !entityManager.createQuery("SELECT d FROM DrivingExam d WHERE d.student = :student AND d.examDate > CURRENT_DATE AND d.examResult IS NULL", DrivingExam.class)
                .setParameter("student", student)
                .getResultList().isEmpty();
    }

    public DrivingExam getUpcomingExam(User student){
        return entityManager.createQuery("SELECT d FROM DrivingExam d WHERE d.student = :student AND d.examDate > CURRENT_DATE AND d.examResult IS NULL", DrivingExam.class)
                .setParameter("student", student)
                .getSingleResult();
    }
}
