package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService theProductService) {
        productService = theProductService;
    }

    @GetMapping("/products")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/products/{prouductId}")
    public Product findById(@PathVariable int prouductId) {
        Product product = productService.findById(prouductId);
        if (product == null) {
            throw new RuntimeException("Employee id not found - " + prouductId);
        }
        return product;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product theProduct) {

        theProduct.setId(0);

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }

    @PostMapping("/product")
    public Product addProductWithNoBuyNow(@RequestBody Product theProduct) {

        theProduct.setId(0);

        theProduct.setBuyNow(0);

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product theProduct) {

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }

    @DeleteMapping("/products/{prouductId}")
    public String deleteProduct(@PathVariable int prouductId) {

        Product tempProduct = productService.findById(prouductId);

        // throw exception if null

        if (tempProduct == null) {
            throw new RuntimeException("Product id not found - " + prouductId);
        }

        productService.deleteById(prouductId);

        return "Deleted prouduct id - " + prouductId;
    }

}
