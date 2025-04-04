package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.CategoryRequest;
import com.BackEnd.BidPro.Dto.Response.CategoryResponse;
import com.BackEnd.BidPro.Model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long theId);

    CategoryResponse findCategoryAndProductsByCategoryId(Long theId);

    void addCategory(CategoryRequest categoryRequest);


    Category save(Category category);

    void deleteCategory(Long id);


}
