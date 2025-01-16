package com.herbak.marcell.beadando.dao;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class AbstractDao<T> implements Dao<T> {
    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    public AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public void add(T t) {
        entityManager.persist(t);
    }

    @Override
    @Transactional
    public void update(T t) {
        entityManager.merge(t);
    }

    @Override
    @Transactional
    public void delete(T t) {
        entityManager.remove(entityManager.contains(t) ? t : entityManager.merge(t));
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        T entity = get(id);
        if (entity != null) {
            delete(entity);
        }
    }

    @Override
    public T get(int id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> getAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e", entityClass)
                .getResultList();
    }

}
