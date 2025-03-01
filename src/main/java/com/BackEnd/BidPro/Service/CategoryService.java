package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Model.Product;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(int theId);

    Category save(Category category);

    void deleteById(int theId);
}
