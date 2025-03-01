package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService theCategoryService) {
        categoryService = theCategoryService;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/categories/{categoryId}")
    public Category findById(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            throw new RuntimeException("Employee id not found - " + categoryId);
        }
        return category;
    }

    @GetMapping("/categoriess/{categoryId}")
    public Category findCategoryAndProductsByCategoryId(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);

        if (category == null) {
            throw new RuntimeException("Employee id not found - " + categoryId);
        }
        System.out.println(category);
//        System.out.println(category.getProducts());
        return category;
    }

    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category theCategory) {

        theCategory.setId(0L);

        Category dbCategory = categoryService.save(theCategory);

        return dbCategory;
    }


    @PutMapping("/categories")
    public Category updateCategory(@RequestBody Category theCategory) {

        Category dbCategory = categoryService.save(theCategory);

        return dbCategory;
    }

    @DeleteMapping("/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {

        Category tempCategory = categoryService.findById(categoryId);

        // throw exception if null

        if (tempCategory == null) {
            throw new RuntimeException("Category id not found - " + categoryId);
        }

        categoryService.deleteById(categoryId);

        return "Deleted Category id - " + categoryId;
    }

}
