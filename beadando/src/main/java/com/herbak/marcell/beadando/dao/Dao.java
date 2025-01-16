package com.herbak.marcell.beadando.dao;

import java.util.List;

public interface Dao<T> {
    void add(T t);
    void update(T t);
    void delete(T t);
    void deleteById(int id);
    T get(int id);
    List<T> getAll();
}
