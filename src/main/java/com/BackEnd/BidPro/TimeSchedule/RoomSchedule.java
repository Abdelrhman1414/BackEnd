package com.BackEnd.BidPro.TimeSchedule;

import com.BackEnd.BidPro.Model.BidOnProduct;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.ProductRepo;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.ProductService;

import com.BackEnd.BidPro.notifications.NotificaionService;
import com.BackEnd.BidPro.notifications.Notification;
import com.BackEnd.BidPro.notifications.NotificationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoomSchedule {
    private final UserRepository userRepository;
    ProductService productService;
    ProductRepo productRepo;
    NotificaionService notificationService;
    NotificationRepository notificationRepository;
    @Autowired
    public RoomSchedule(ProductService productService, ProductRepo productRepo, UserRepository userRepository , NotificaionService notificationService, NotificationRepository notificationRepository) {
        this.productService = productService;
        this.productRepo = productRepo;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    //@Scheduled(cron = "0 0 * * * *") //every Hour
//    @Scheduled(cron = "0 0 0 * * *") //every Day At 12 AM (00:00)
    @Scheduled(cron = "*/5 * * * * *\n")//every 5 sec
    @Transactional
    public void checkForEndDate() {
        System.out.println("Checking for end date");
        boolean flag =false;
        Date date = new Date();
        List<Product> products = productRepo.findLiveProducts(date);
        for (Product product : products) {
            if (product.getEndDate().getTime() <= date.getTime()) {
                if (productService.IsInRoom(product.getId())) {
                    BidOnProduct bidOnProduct = productService.findInRoom(product.getId());
                    User seller = product.getSeller();
                    User winner = bidOnProduct.getUserId();
                    if (product.getBuyNow() < 20000) {
                        seller.setBalance((long) (seller.getBalance() + bidOnProduct.getHighestBid() + product.getInsuranceAmount()));
                        // notification for user won < 20000
                        winner.setBuying(winner.getBuying() + 1);
                        seller.setSelling(seller.getSelling() + 1);
                        userRepository.save(winner);
                        userRepository.save(seller);
                        product.setAvailable(false);
                        product.setBuyerId(winner.getId());

                        Notification notification = new Notification();
                        notification.setMessage("Congratulations, " + winner.getName() + " You’ve won the bid for the product " +  product.getTitle() + ", The product will be shipped to you within 7 days ");
                        notificationService.sendNotification(String.valueOf(winner.getId()), notification);
                        notification.setUser(winner);
                        notificationRepository.save(notification);


                        Notification notification2 = new Notification();
                        List<User> productUsers = product.users;
                        // notification for users paid insurance
                        for (User productUser : productUsers) {
                            if (productService.ifThisUserPaidInsurance(product.getId(), productUser.getId())) {
                                if (!productUser.getId().equals(winner.getId())) {
                                    productUser.setBalance(productUser.getBalance() + (long) product.getInsuranceAmount());
                                    userRepository.save(productUser);

                                    notification2.setMessage(productUser.getName() + " the product " + product.getTitle() + " has been ended , another customer won it !");
                                    notificationService.sendNotification(String.valueOf(productUser.getId()), notification2);
                                    notification2.setUser(productUser);
                                    notificationRepository.save(notification2);
                                }
                            }
                        }
                        productUsers.clear();
                        try {
                            productService.deleteRoom(bidOnProduct);
                            System.out.println("Successfully deleted room: " + bidOnProduct.getId());
                        } catch (Exception e) {
                            System.err.println("Failed to delete room: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        seller.setBalance((long) (seller.getBalance() + product.getInsuranceAmount()));

                        product.setAvailable(false);
                        product.setProcessing(true);
                        Notification notification = new Notification();
                        notification.setMessage("Congratulations, " + winner.getName() + " You’ve won the bid for the product " +  product.getTitle() + ",  member of our team will contact you and the seller to complete the remaining process ");
                        notificationService.sendNotification(String.valueOf(winner.getId()), notification);
                        notification.setUser(winner);
                        notificationRepository.save(notification);

                        // winner waits admin approve
                        winner.setBuying(winner.getBuying() + 1);
                        seller.setSelling(seller.getSelling() + 1);


                        if (winner.getBuying()>=5 &&!flag) {
                            winner.setVerified(true);
                        }
                        if (seller.getSelling()>=5 &&!flag) {
                            seller.setVerified(true);
                        }

                        userRepository.save(winner);
                        userRepository.save(seller);
                        if (winner.getBuying() % 5 == 0) {
                            winner.setBalance(winner.getBalance() + 1000);
                        }
                        if (seller.getSelling() % 5 == 0) {
                            seller.setBalance(seller.getBalance() + 1000);
                        }
                        productRepo.save(product);

                        List<User> productUsers = product.users;
                        // notification for users paid insurance
                        Notification notification2 = new Notification();
                        for (User productUser : productUsers) {
                            System.out.println(productUser.getName());
                            if (productService.ifThisUserPaidInsurance(product.getId(), productUser.getId())) {
                                if (!productUser.getId().equals(winner.getId())) {
                                    productUser.setBalance(productUser.getBalance() + (long) product.getInsuranceAmount());
                                    userRepository.save(productUser);

                                    notification2.setMessage(productUser.getName() + " the product " + product.getTitle() + " has been ended , another customer won it !");
                                    notificationService.sendNotification(String.valueOf(productUser.getId()), notification2);
                                    notification2.setUser(productUser);
                                    notificationRepository.save(notification2);
                                }
                            }
                        }
                        productUsers.clear();
                    }
                    productRepo.save(product);
                }
            }
        }
    }
}
