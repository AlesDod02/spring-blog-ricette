package com.learning.project.springblogricette.controller;

import com.learning.project.springblogricette.model.Category;
import com.learning.project.springblogricette.model.Recipes;
import com.learning.project.springblogricette.repository.CategoryRepository;
import com.learning.project.springblogricette.repository.RecipesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private RecipesRepository recipesRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping
    public String index(Model model){
        List<Category> categoryList;
        categoryList=categoryRepository.findAll();
        model.addAttribute("category", categoryList);
        return "category/list";
    }
    @GetMapping("/create")
    public String create(Model model) {
        Category category = new Category();

        model.addAttribute("category", category);

        return "category/create";
    }
    @PostMapping("/create")
    public String category(@Valid @ModelAttribute("category") Category formCategory, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());

            return "category/create";
        }
        Category savedCategory = categoryRepository.save(formCategory);

        return "redirect:/category";

    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("category", result.get());

            return "category/edit";

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("category") Category formcategory, BindingResult bindingResult) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isPresent()) {
            Category categoryToEdit = result.get();
            if (bindingResult.hasErrors()) {
                return "category/edit";
            }


            Category savedCategory = categoryRepository.save(formcategory);
            return "redirect:/category";

        }

        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category with id " + id + " not found");
        }
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // verifico se il Book è presente su db
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isPresent()) {
            // se c'è lo cancello
            categoryRepository.deleteById(id);
            // mando un messaggio di successo con la redirect
            redirectAttributes.addFlashAttribute("redirectMessage",
                    "Category " + result.get().getName() + " deleted!");
            return "redirect:/category";
        } else {
            // se non c'è sollevo un'eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category with di " + id + " not found");
        }
    }

}
