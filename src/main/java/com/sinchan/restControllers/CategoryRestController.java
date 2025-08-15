package com.sinchan.restControllers;

import com.sinchan.entities.Category;
import com.sinchan.entities.User;
import com.sinchan.services.CategoryService;
import com.sinchan.user.credentials.SinchanAuthToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("category/list")
    public List<Category> categoryList(){

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        User user = authToken.getUser();

        return categoryService.listCategory(user.getUserId());
    }

}
