package com.herbak.marcell.beadando.entity;

import jakarta.persistence.*;

@Entity
@Table(name="driving_paths")
public class DrivingPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pathName;

    @Column(nullable = false)
    private int pathLength;

    public Long getId() {
        return id;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }
}
