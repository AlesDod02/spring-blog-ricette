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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipes> getRecipesList() {
        return recipesList;
    }

    public void setRecipesList(List<Recipes> recipesList) {
        this.recipesList = recipesList;
    }
}
