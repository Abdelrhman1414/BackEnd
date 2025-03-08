package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();

    ProductResponse findByIdResponse(long theId);


    Product findById(Long theId);

    Product save(Product product);


    ResponseEntity<?> addProduct(ProductRequest productRequest);

    void deleteById(Long theId);

    ResponseEntity<?> insuranceAmountHandling(long theId);

}
