package com.herbak.marcell.beadando.entity;

import jakarta.persistence.*;

@Entity
@Table(name="driving_paths")
public class DrivingPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pathName;

    @Column(nullable = false)
    private int pathLength;
}
