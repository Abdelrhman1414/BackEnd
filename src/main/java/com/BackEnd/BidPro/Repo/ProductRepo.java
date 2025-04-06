package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

//    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
//    List<Product> findProductsByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE " +
            "p.startDate <= :currentDate AND " +
            "p.available = true AND " +
            "p.isPending = false AND " +
            "p.processing = false")
    List<Product> findLiveProducts(@Param("currentDate") Date currentDate);

}
