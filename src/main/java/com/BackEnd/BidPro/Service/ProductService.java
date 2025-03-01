package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    Product findById(int theId);

    Product save(Product product);

    void deleteById(int theId);
}
