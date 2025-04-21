package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.NewHeghestPrice;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Dto.Response.RoomResponse;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.ProductService;
import com.BackEnd.BidPro.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    // For Guests
    @GetMapping("/products")
    public ResponseEntity<?> findAll() {
        try {
            List<ProductResponse> productResponse = productService.findAll();
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //For Users
    @GetMapping("/productsForUsers")
    public ResponseEntity<?> findAllForUsres() {
        try {
            List<ProductResponse> productResponse = productService.findAllForUsers();
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
    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@ModelAttribute ProductRequest productRequest) {
        try {
            productService.addProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Adding product WithOut BuyNow
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProductWithNoBuyNow(@ModelAttribute ProductRequest productRequest) {
        try {
            productRequest.setBuyNow(String.valueOf(0));
            productService.addProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully WithOut buyNow");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product theProduct) {

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }


//    @DeleteMapping("/products/{prouductId}")
//    public ResponseEntity<?> deleteProduct(@PathVariable long prouductId) {
//        Product tempProduct = productService.findById(prouductId);
//        // throw exception if null
//        if (tempProduct == null) {
//            throw new RuntimeException("Product id not found - " + prouductId);
//        }
//        productService.deleteById(prouductId);
//        return new ResponseEntity<>("Product has been deleted successfully", HttpStatus.OK);
//    }

    // paying the insurance amount
    @GetMapping("/insurance/{prouductId}")
    public ResponseEntity<?> insuranceAmountHandling(@PathVariable long prouductId) {
        return new ResponseEntity<>(productService.insuranceAmountHandling(prouductId), HttpStatus.OK);
    }

    // if the user paid the insurance?
    @GetMapping("/paidinsurance/{prouductId}")
    public ResponseEntity<?> paidInsurance(@PathVariable long prouductId) {
        return new ResponseEntity<>(productService.paidInsurance(prouductId), HttpStatus.OK);
    }

    // paying product with BuyNow
    @GetMapping("/buyNow/{prouductId}")
    public ResponseEntity<?> buyingProductWithBuyNow(@PathVariable long prouductId) {
        return new ResponseEntity<>(productService.buyingWithBuyNow(prouductId), HttpStatus.OK);
    }

    //Find all Products That I Have Published
    @GetMapping("/myPosts")
    public ResponseEntity<?> findMyPosts() {

        return new ResponseEntity<>(productService.findMyPosts(), HttpStatus.OK);
    }

    //find Products that I have bid on it
    @GetMapping("/myBids")
    public ResponseEntity<?> findMyBids() {
        return new ResponseEntity<>(productService.findMyBids(), HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> findAllRoom() {
        return new ResponseEntity<>(productService.findAllBidOnProduct(), HttpStatus.OK);
    }

    @GetMapping("/room/{prouductId}")
    public ResponseEntity<?> findRoom(@PathVariable long prouductId) {
        return new ResponseEntity<>(productService.findRoomWithID(prouductId), HttpStatus.OK);
    }

    @PutMapping("/bid/{prouductId}")
    public ResponseEntity<?> updateProductInRoom(@RequestBody NewHeghestPrice newHeghestPrice,@PathVariable long prouductId) {
        return new ResponseEntity<>(productService.updateRoom(productService.findById(prouductId), newHeghestPrice.getPrice()), HttpStatus.OK);

    }

    @PostMapping("bidNow/{prouductId}")
    public ResponseEntity<?> bidOnTheProudect(@RequestBody NewHeghestPrice newHeghestPrice, @PathVariable long prouductId) {

        return new ResponseEntity<>(productService.addToRoom(productService.findById(prouductId), newHeghestPrice.getPrice()), HttpStatus.OK);
    }


}
