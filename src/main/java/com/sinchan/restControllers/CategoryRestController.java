package com.sinchan.restControllers;

import com.sinchan.entities.Category;
import com.sinchan.services.CategoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("category/list")
    public List<Category> categoryList(){
        System.out.println("Category list called.");

        return categoryService.listCategory(100);
    }

}
