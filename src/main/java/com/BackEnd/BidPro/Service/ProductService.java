package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.NewHeghestPrice;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Dto.Response.RoomResponse;
import com.BackEnd.BidPro.Model.BidOnProduct;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();

    List<ProductResponse> findAllForUsers();


    List<ProductResponse> getAllPendingProducts();

    void approveProductById(Long id);
    void declineProductById(Long id);

    List<ProductResponse> getAllProcessingProducts();
    List<ProductResponse> getAllLiveProducts();

    ProductResponse findByIdResponse(long theId);


    Product findById(Long theId);

    Product save(Product product);


    void addProduct(ProductRequest productRequest) throws IOException, ParseException;

    void deleteById(Long theId);

    ResponseEntity<?> insuranceAmountHandling(long theId);

    Boolean paidInsurance(long theId);

    ResponseEntity<?> buyingWithBuyNow(long theId);

    List<ProductResponse> findMyPosts();

    List<ProductResponse> findMyBids();

    List<RoomResponse> findAllBidOnProduct();

    RoomResponse findRoomWithID(long theId);

    BidOnProduct saveRoom(BidOnProduct bidOnProduct);

    ResponseEntity<?> updateRoom(Product product, float newPrice);

    ResponseEntity<?> addToRoom(Product product,float newPrice);

    Boolean IsInRoom(long theId);

    BidOnProduct findInRoom(long productId);

    void deleteRoom(BidOnProduct bidOnProduct);

    Boolean ifThisUserPaidInsurance(long productId, long userId);

    void sendbid(List<String> list);



}
