package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.Product;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();

    ProductResponse findByIdResponse(long theId);


    Product findById(Long theId);

    Product save(Product product);


    void addProduct(ProductRequest productRequest) throws IOException, ParseException;

    void deleteById(Long theId);

    ResponseEntity<?> insuranceAmountHandling(long theId);

    Boolean paidInsurance(long theId);

    ResponseEntity<?> buyingWithBuyNow(long theId);






}
