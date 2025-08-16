package com.sinchan.restControllers;

import com.sinchan.entities.Category;
import com.sinchan.entities.User;
import com.sinchan.services.CategoryService;
import com.sinchan.user.credentials.SinchanAuthToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;

@RestController
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("category/list")
    public List<Category> categoryList(HttpServletRequest request){

        Locale locale = RequestContextUtils.getLocale(request);

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        User user = authToken.getUser();

        return categoryService.listCategory(user.getUserId(), locale.getLanguage());
    }

}
