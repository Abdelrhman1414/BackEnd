package com.BackEnd.BidPro.TimeSchedule;

import com.BackEnd.BidPro.Model.BidOnProduct;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.ProductRepo;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.ProductService;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoomSchedule {
    private final UserRepository userRepository;
    ProductService productService;
    ProductRepo productRepo;

    @Autowired
    public RoomSchedule(ProductService productService, ProductRepo productRepo, UserRepository userRepository) {
        this.productService = productService;
        this.productRepo = productRepo;
        this.userRepository = userRepository;
    }

    //@Scheduled(cron = "0 0 * * * *") every Hour
    //@Scheduled(cron = "0 0 0 * * *") every Day At 12 AM (00:00)
    @Scheduled(cron = "*/5 * * * * *\n")
    public void checkForEndDate() {
        System.out.println("Checking for end date");
        Date date = new Date();
        List<Product> products = productRepo.findLiveProducts(date);
        for (Product product : products) {
            if (product.getEndDate().getTime() <= date.getTime()) {
                if (productService.IsInRoom(product.getId())) {
                    BidOnProduct bidOnProduct = productService.findInRoom(product.getId());
                    User seller = product.getSeller();
                    if (product.getBuyNow() < 20000) {
                        User winner = bidOnProduct.getUserId();
                        winner.setBalance((long) (winner.getBalance() - bidOnProduct.getHighestBid()));
                        seller.setBalance((long) (seller.getBalance() + bidOnProduct.getHighestBid() + product.getInsuranceAmount()));
                        userRepository.save(winner);
                        userRepository.save(seller);
                        product.setAvailable(false);
                        product.setBuyerId(winner.getId());
                        List<User> productUsers = product.users;
                        for (User productUser : productUsers) {
                            if (productService.ifThisUserPaidInsurance(product.getId(), productUser.getId())) {
                                if (!productUser.getId().equals(winner.getId())) {
                                    productUser.setBalance(productUser.getBalance() + (long) product.getInsuranceAmount());
                                    userRepository.save(productUser);
                                }
                            }
                        }
                        productService.deleteRoom(bidOnProduct);
                    } else {
                        seller.setBalance((long) (seller.getBalance() + product.getInsuranceAmount()));
                        userRepository.save(seller);
                        product.setAvailable(false);
                        product.setProcessing(true);
                    }
                    productRepo.save(product);
                }
                List<User> productUsers = product.users;
                for (User productUser : productUsers) {
                    System.out.println(productUser.getName());
                    if (productService.ifThisUserPaidInsurance(product.getId(), productUser.getId())) {
                        productUser.setBalance(productUser.getBalance() + (long) product.getInsuranceAmount());
                        userRepository.save(productUser);

                    }
                }
                productRepo.delete(product);
            }
        }
    }

}
