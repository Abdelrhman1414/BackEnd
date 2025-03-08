package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
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
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/products/{prouductId}")
    public Product findById(@PathVariable Long prouductId) {
        Product product = productService.findById(prouductId);
        if (product == null) {
            throw new RuntimeException("Employee id not found - " + prouductId);
        }
        return product;
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
        return new ResponseEntity<>(productService.addProduct(productRequest), HttpStatus.OK);
    }

    @PostMapping("/product")
    public Product addProductWithNoBuyNow(@RequestBody Product theProduct) {

        theProduct.setId(0L);

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
    public String deleteProduct(@PathVariable Long prouductId) {

        Product tempProduct = productService.findById(prouductId);

        // throw exception if null

        if (tempProduct == null) {
            throw new RuntimeException("Product id not found - " + prouductId);
        }

        productService.deleteById(prouductId);

        return "Deleted prouduct id - " + prouductId;
    }

    @GetMapping("/insurance/{prouductId}")
    public String insuranceAmountHandling(@PathVariable Long prouductId) {
        Product product = findById(prouductId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));

        float productAmount = product.getInsuranceAmount();

        Long userBalance = user.getBalance();
        Category category = product.getCategory();
        List<Category> interests = user.getCategoryList();
        boolean alreadyHave = false;
        for( Category interest : interests ) {
            if(interest.getId()== category.getId()){
                alreadyHave=true;
            }
        }
        if(!alreadyHave){
            interests.add(category);
            user.setCategoryList(interests);
            userRepository.save(user);
        }

        if (userBalance > productAmount) {
            user.setBalance((long) (userBalance - productAmount));
            product.addUser(user);
            productService.save(product);

            return ("Done!");
        } else

            return ("your balance is not enough");
    }

}
