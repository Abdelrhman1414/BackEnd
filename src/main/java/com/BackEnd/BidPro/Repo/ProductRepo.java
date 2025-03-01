package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
