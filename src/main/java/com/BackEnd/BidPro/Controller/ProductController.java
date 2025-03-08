package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.ProductService;
import com.BackEnd.BidPro.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public ProductController(ProductService theProductService, UserService theUserService, UserRepository theUserRepository) {
        productService = theProductService;
        userService = theUserService;
        userRepository = theUserRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<?> findAll() {
        try {
            List<ProductResponse> productResponse = productService.findAll();
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/products/{prouductId}")
    public ResponseEntity<?> findById(@PathVariable long prouductId) {
        try {
            ProductResponse productResponse = productService.findByIdResponse(prouductId);

            if (productResponse == null) {
                throw new RuntimeException("Product id not found - " + prouductId);
            }
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product theProduct) {

        theProduct.setId(0L);

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }

    // add Product with photo
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(ProductRequest productRequest) {
        try {
            productService.addProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        }
        catch (Exception e) {
            System.out.println("here");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProductWithNoBuyNow(ProductRequest productRequest) {
        try {
            productRequest.setBuyNow(String.valueOf(0));
            productService.addProduct(productRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product theProduct) {

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }


    @DeleteMapping("/products/{prouductId}")
    public ResponseEntity<?> deleteProduct(@PathVariable long prouductId) {

        Product tempProduct = productService.findById(prouductId);

        // throw exception if null

        if (tempProduct == null) {
            throw new RuntimeException("Product id not found - " + prouductId);
        }

        productService.deleteById(prouductId);

        return new ResponseEntity<>("Product has been deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/insurance/{prouductId}")
    public ResponseEntity<?> insuranceAmountHandling(@PathVariable long prouductId) {
        return new ResponseEntity<>(productService.insuranceAmountHandling(prouductId), HttpStatus.OK);
    }

}
