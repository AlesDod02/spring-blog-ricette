package com.learning.project.springblogricette.repository;

import com.learning.project.springblogricette.model.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipesRepository extends JpaRepository<Recipes, Integer> {
    List<Recipes> findByTitleContaining(String searchName);
}
