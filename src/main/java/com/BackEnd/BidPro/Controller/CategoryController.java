package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.CategoryRequest;
import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.CategoryResponse;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllCategories() {
        try {
            List<CategoryResponse> categories = categoryService.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> findById(@PathVariable long categoryId) {
        try {
            CategoryResponse categoryResponse = categoryService.findById(categoryId);

            if (categoryResponse == null) {
                throw new RuntimeException("Category id not found - " + categoryId);
            }
            return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/categoriess/{categoryId}")
    public ResponseEntity<?> findCategoryAndProductsByCategoryId(@PathVariable long categoryId) {
       try{
        CategoryResponse categoryResponse = categoryService.findCategoryAndProductsByCategoryId(categoryId);

        if (categoryResponse == null) {
            throw new RuntimeException("Category id not found - " + categoryId);
        }
        System.out.println(categoryResponse);
      return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/categories")
//    public Category addCategory(@RequestBody Category theCategory) {
//
//        theCategory.setId(0L);
//
//        Category dbCategory = categoryService.save(theCategory);
//
//        return dbCategory;
//    }




    @PutMapping("/categories")
    public Category updateCategory(@RequestBody Category theCategory) {

        Category dbCategory = categoryService.save(theCategory);

        return dbCategory;
    }

//    @DeleteMapping("/categories/{categoryId}")
//    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
//
//        CategoryResponse tempCategory = categoryService.findById(categoryId);
//
//        // throw exception if null
//
//        if (tempCategory == null) {
//            throw new RuntimeException("Category id not found - " + categoryId);
//        }
//
//        categoryService.deleteById(categoryId);
//
//        return new ResponseEntity<>("Category has been deleted successfully", HttpStatus.OK);
//    }

}
