package com.learning.project.springblogricette.controller;

import jakarta.validation.Valid;
import com.learning.project.springblogricette.model.Recipes;
import com.learning.project.springblogricette.repository.CategoryRepository;
import com.learning.project.springblogricette.repository.RecipesRepository;
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
@RequestMapping("/recipes")
public class RecipesController {
    @Autowired
    private RecipesRepository recipesRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String searchKeyword, Model model) {
        List<Recipes> recipesList;
        if (searchKeyword != null) {
            recipesList = recipesRepository.findByTitleContaining(searchKeyword);

        } else {
            recipesList = recipesRepository.findAll();
        }
    model.addAttribute("recipesList", recipesList);

    model.addAttribute("preloadSearch", searchKeyword);
    return "recipes/list";
    }
    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model){
        Optional<Recipes> result = recipesRepository.findById(id);
        if (result.isPresent()){
            Recipes recipe = result.get();
            model.addAttribute("recipe", recipe);
            return "/recipes/show";
        }
        else {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }
    @GetMapping("/create")
    public String create(Model model) {
        Recipes recipe = new Recipes();

        model.addAttribute("recipe", recipe);
        model.addAttribute("category", categoryRepository.findAll());
        return "recipes/create";
    }

    @PostMapping("/create")
    public String create2(@Valid @ModelAttribute("recipes") Recipes formRecipe, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());

            return "recipe/create";
        }
        Recipes savedRecipe = recipesRepository.save(formRecipe);


        return "redirect:/recipes/show/" + savedRecipe.getId();
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Recipes> result = recipesRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("recipe", result.get());
            model.addAttribute("category", categoryRepository.findAll());
            return "recipes/edit";

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("recipe") Recipes formRecipe, BindingResult bindingResult) {
        Optional<Recipes> result = recipesRepository.findById(id);
        if (result.isPresent()) {
            Recipes recipeToEdit = result.get();
            if (bindingResult.hasErrors()) {
                return "/recipes/edit";
            }
            formRecipe.setUrl(recipeToEdit.getUrl());
            Recipes savedRecipe = recipesRepository.save(formRecipe);
            return "redirect:/recipes/show/{id}";

        }

        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id " + id + " not found");
        }
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // verifico se il Book è presente su db
        Optional<Recipes> result = recipesRepository.findById(id);
        if (result.isPresent()) {
            // se c'è lo cancello
            recipesRepository.deleteById(id);
            // mando un messaggio di successo con la redirect
            redirectAttributes.addFlashAttribute("redirectMessage",
                    "Recipe " + result.get().getTitle() + " deleted!");
            return "redirect:/recipes";
        } else {
            // se non c'è sollevo un'eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with di " + id + " not found");
        }
    }

}
