package com.learning.project.springblogricette.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Recipes> recipesList;

}
