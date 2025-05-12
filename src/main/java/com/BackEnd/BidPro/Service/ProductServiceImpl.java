package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.NewHeghestPrice;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Dto.Response.RoomResponse;
import com.BackEnd.BidPro.Model.*;
import com.BackEnd.BidPro.Repo.CategoryRepo;
import com.BackEnd.BidPro.Repo.ProductRepo;
import com.BackEnd.BidPro.Repo.RoomRepo;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.cloudinary.model.Image;
import com.BackEnd.BidPro.cloudinary.service.CloudinaryService;
import com.BackEnd.BidPro.cloudinary.service.ImageService;
import com.BackEnd.BidPro.notifications.NotificaionServicee;
import com.BackEnd.BidPro.notifications.Notification;
import com.BackEnd.BidPro.notifications.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

import java.util.*;
import java.text.SimpleDateFormat;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ImageService imageService;
    private final CloudinaryService cloudinaryService;
    private final CategoryRepo categoryRepo;
    private final UserRepository userRepository;
    private final RoomRepo roomRepo;
    private final NotificationRepository notificationRepository;
    private final NotificaionServicee notificationService;


    @Override
    public List<ProductResponse> findAll() {

        List<Product> products = productRepo.findAll();
        List<ProductResponse> productResponses1 = new ArrayList<>();

        for (Product product : products) {
            if (product.getAvailable()) {
                ProductResponse productResponse = new ProductResponse();
                productResponse.setID(String.valueOf(product.getId()));
                productResponse.setDescription(product.getDescription());
                productResponse.setTitle(product.getTitle());
                productResponse.setQuantity(String.valueOf(product.getQuantity()));
                productResponse.setStartPrice(String.valueOf(product.getStartPrice()));
                productResponse.setInsuranceAmount(String.valueOf(product.getInsuranceAmount()));
                productResponse.setIncrementbid(String.valueOf(product.getIncrementbid()));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                productResponse.setStartDate(formatter.format(product.getStartDate()));
                productResponse.setEndDate(formatter.format(product.getEndDate()));
                productResponse.setBuyNow(String.valueOf(product.getBuyNow()));

                productResponse.setCategoryName(product.getCategory().getName());


                productResponse.setSellerName(product.getSeller().getName());
                List<String> urls = new ArrayList<>();
                for (Image image : product.getImages()) {
                    urls.add(image.getUrl());
                }
                productResponse.setUrls(urls);
                productResponses1.add(productResponse);
            }
        }
        return productResponses1;
    }

    @Override
    public List<ProductResponse> findAllForUsers() {
        List<ProductResponse> productResponses = findAll();

        for (ProductResponse productResponse : productResponses) {
            if (paidInsurance(Long.parseLong(productResponse.getID()))) {
                productResponse.setUserBidOnThisProduct(true);
            }
        }

        return productResponses;
    }

    @Override
    public List<ProductResponse> getAllPendingProducts() {

        List<Product> products = productRepo.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            if (!product.getAvailable() && product.getIsPending() && !product.getProcessing()) {
                ProductResponse productResponse = new ProductResponse();
                productResponse.setID(String.valueOf(product.getId()));
                productResponse.setDescription(product.getDescription());
                productResponse.setTitle(product.getTitle());
                productResponse.setQuantity(String.valueOf(product.getQuantity()));
                productResponse.setStartPrice(String.valueOf(product.getStartPrice()));
                productResponse.setInsuranceAmount(String.valueOf(product.getInsuranceAmount()));
                productResponse.setIncrementbid(String.valueOf(product.getIncrementbid()));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                productResponse.setStartDate(formatter.format(product.getStartDate()));
                productResponse.setEndDate(formatter.format(product.getEndDate()));
                productResponse.setBuyNow(String.valueOf(product.getBuyNow()));

                productResponse.setCategoryName(product.getCategory().getName());


                productResponse.setSellerName(product.getSeller().getName());
                List<String> urls = new ArrayList<>();
                for (Image image : product.getImages()) {
                    urls.add(image.getUrl());
                }
                productResponse.setUrls(urls);
                productResponse.setPending(product.getIsPending());

                productResponses.add(productResponse);

            }
        }

        return productResponses;
    }

    @Override
    public void approveProductById(Long id) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent() && !product.get().getAvailable() && product.get().getIsPending() && !product.get().getProcessing()) {
            product.get().setAvailable(true);
            product.get().setIsPending(false);

        } else {
            throw new RuntimeException("Did not find product id - " + id);
        }

    }

    @Override
    public void declineProductById(Long id) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent() && !product.get().getAvailable() && product.get().getIsPending() && !product.get().getProcessing()) {
            productRepo.delete(product.get());
        } else {
            throw new RuntimeException("Did not find product id - " + id);
        }

    }

    @Override
    public List<ProductResponse> getAllProcessingProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            if (!product.getAvailable() && !product.getIsPending() && product.getProcessing()) {
                ProductResponse productResponse = new ProductResponse();
                productResponse.setID(String.valueOf(product.getId()));
                productResponse.setDescription(product.getDescription());
                productResponse.setTitle(product.getTitle());
                productResponse.setQuantity(String.valueOf(product.getQuantity()));
                productResponse.setStartPrice(String.valueOf(product.getStartPrice()));
                productResponse.setInsuranceAmount(String.valueOf(product.getInsuranceAmount()));
                productResponse.setIncrementbid(String.valueOf(product.getIncrementbid()));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                productResponse.setStartDate(formatter.format(product.getStartDate()));
                productResponse.setEndDate(formatter.format(product.getEndDate()));
                productResponse.setBuyNow(String.valueOf(product.getBuyNow()));

                productResponse.setCategoryName(product.getCategory().getName());


                productResponse.setSellerName(product.getSeller().getName());
                List<String> urls = new ArrayList<>();
                for (Image image : product.getImages()) {
                    urls.add(image.getUrl());
                }
                productResponse.setUrls(urls);
                productResponse.setPending(product.getIsPending());

                productResponses.add(productResponse);

            }
        }

        return productResponses;
    }

    @Override
    public List<ProductResponse> getAllLiveProducts() {
        SimpleDateFormat formatterr = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        formatterr.format(date);
        List<Product> products = productRepo.findLiveProducts(date);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {

            ProductResponse productResponse = new ProductResponse();
            productResponse.setID(String.valueOf(product.getId()));
            productResponse.setDescription(product.getDescription());
            productResponse.setTitle(product.getTitle());
            productResponse.setQuantity(String.valueOf(product.getQuantity()));
            productResponse.setStartPrice(String.valueOf(product.getStartPrice()));
            productResponse.setInsuranceAmount(String.valueOf(product.getInsuranceAmount()));
            productResponse.setIncrementbid(String.valueOf(product.getIncrementbid()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            productResponse.setStartDate(formatter.format(product.getStartDate()));
            productResponse.setEndDate(formatter.format(product.getEndDate()));
            productResponse.setBuyNow(String.valueOf(product.getBuyNow()));

            productResponse.setCategoryName(product.getCategory().getName());


            productResponse.setSellerName(product.getSeller().getName());
            List<String> urls = new ArrayList<>();
            for (Image image : product.getImages()) {
                urls.add(image.getUrl());
            }
            productResponse.setUrls(urls);
            productResponse.setPending(product.getIsPending());
            productResponses.add(productResponse);

        }


        return productResponses;
    }


    @Override
    public ProductResponse findByIdResponse(long theId) {
        Optional<Product> result = productRepo.findById(theId);

        ProductResponse productResponse = new ProductResponse();

        Product product = null;
        if (result.isPresent()) {
            product = result.get();
        } else {
            throw new RuntimeException("Did not find product id - " + theId);
        }
        productResponse.setID(String.valueOf(product.getId()));
        productResponse.setDescription(product.getDescription());
        productResponse.setTitle(product.getTitle());
        productResponse.setQuantity(String.valueOf(product.getQuantity()));
        productResponse.setStartPrice(String.valueOf(product.getStartPrice()));
        productResponse.setInsuranceAmount(String.valueOf(product.getInsuranceAmount()));
        productResponse.setIncrementbid(String.valueOf(product.getIncrementbid()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        productResponse.setStartDate(formatter.format(product.getStartDate()));
        productResponse.setEndDate(formatter.format(product.getEndDate()));
        productResponse.setBuyNow(String.valueOf(product.getBuyNow()));

        productResponse.setCategoryName(product.getCategory().getName());
        User user = userRepository.findById(product.getSeller().getId()).orElse(null);
        productResponse.setSellerName(user.getName());

        productResponse.setSellerName(user.getName());
        List<String> urls = new ArrayList<>();
        for (Image image : product.getImages()) {
            urls.add(image.getUrl());
        }
        productResponse.setUrls(urls);

        return productResponse;
    }

    @Override
    public Product findById(Long theId) {
        Optional<Product> result = productRepo.findById(theId);

        Product product = null;
        if (result.isPresent()) {
            product = result.get();
        } else {
            throw new RuntimeException("Did not find product id - " + theId);
        }
        return product;
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }


    // adding product with photos .
    @Override
    public void addProduct(ProductRequest productRequest) throws IOException, ParseException {
        List<MultipartFile> multipartFiles = productRequest.getFiles();
        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setDescription(productRequest.getDescription());
        product.setBuyNow(Float.parseFloat(productRequest.getBuyNow()));
        product.setIncrementbid(Float.parseFloat(productRequest.getIncrementbid()));
        product.setInsuranceAmount(Float.parseFloat(productRequest.getInsuranceAmount()));
        product.setQuantity(Integer.parseInt(productRequest.getQuantity()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        product.setStartDate(formatter.parse(productRequest.getStartDate()));
        product.setEndDate(formatter.parse(productRequest.getEndDate()));
        product.setStartPrice(Float.parseFloat(productRequest.getStartPrice()));

        Optional<Category> category = categoryRepo.findById(Long.parseLong(productRequest.getCategoryId()));
        product.setCategory(category.get());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        product.setSeller(user);

        List<Image> images = new ArrayList<>();
        for (MultipartFile image : multipartFiles) {
            Image img = new Image();
            img.setUrl(cloudinaryService.uploadFile(image, "product"));
            images.add(img);
        }
        for (Image image : images) {
            image.setProduct(product);
        }
        product.setImages(images);
        product.setAvailable(false);
        product.setIsPending(true);
        product.setProcessing(false);
        product.setHighestPrice(0);

        productRepo.save(product);
        Notification notification = new Notification();
        notification.setMessage(user.getName() + " your product " + productRequest.getTitle() + " added successfully!");
        notificationService.sendNotification(String.valueOf(user.getId()), notification);
        notification.setUser(user);
        notificationRepository.save(notification);


    }


    @Override
    public void deleteById(Long theId) {
        productRepo.deleteById(theId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<ProductResponse> findMyPosts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        long userId = user.getId();
        List<Product> products = productRepo.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            if (product.getSeller().getId().equals(userId) && product.getAvailable()) {
                productResponses.add(findByIdResponse(product.getId()));
            }

        }
        return productResponses;
    }

    // Find The Posts That I have Bid On It
    @Override
    public List<ProductResponse> findMyBids() {
        List<ProductResponse> productResponses = new ArrayList<>();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        long userId = user.getId();

        List<Product> products = productRepo.findAll();

        for (Product product : products) {
            for (User user1 : product.users) {
                if (user1.getId().equals(userId)) {
                    productResponses.add(findByIdResponse(product.getId()));
                }
            }
        }
        return productResponses;
    }


    @Override
    public ResponseEntity<?> insuranceAmountHandling(long theId) {
        Product product = findById(theId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));

        if (!product.getSeller().getId().equals(user.getId())) {
            float productAmount = product.getInsuranceAmount();

            Long userBalance = user.getBalance();
            Category category = product.getCategory();
            List<Category> interests = user.getCategoryList();
            boolean alreadyHave = false;
            for (Category interest : interests) {
                if (interest.getId() == category.getId()) {
                    alreadyHave = true;
                }
            }
            if (!alreadyHave) {
                interests.add(category);
                user.setCategoryList(interests);
                userRepository.save(user);
            }
            if (product.getAvailable()) {
                if (!paidInsurance(theId)) {
                    if (userBalance >= productAmount) {
                        user.setBalance((long) (userBalance - productAmount));
                        product.addUser(user);
                        save(product);
                        //
                        return ResponseEntity.ok().body("success");
                    } else
                        return ResponseEntity.ok().body("Your balance is not enough :(");
                } else
                    return ResponseEntity.ok().body("You Already Payed the Insurance :)");
            } else
                return ResponseEntity.ok().body("The Product Isn't Available :(");
        } else
            return ResponseEntity.ok().body("You Can't Pay The Insurance Because You Are The Seller :(");
    }


    @Override
    public Boolean paidInsurance(long theId) {
        Product product = findById(theId);
        Boolean paid = false;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));
        long userID = user.getId();
        List<User> productUsers = product.users;
        for (int i = 0; i < productUsers.size(); i++) {
            if (userID == productUsers.get(i).getId()) {
                paid = true;
            }
        }
        return paid;
    }

    @Override
    public Boolean ifThisUserPaidInsurance(long productId, long userId) {
        Product product = findById(productId);
        Boolean paid = false;
        List<User> productUsers = product.users;
        for (User productUser : productUsers) {
            if (userId == productUser.getId()) {
                paid = true;
                break;
            }
        }
        return paid;
    }

    @Override
    public ResponseEntity<?> buyingWithBuyNow(long theId) {
        boolean flag = false;
        Product product = findById(theId);
        User seller = product.getSeller();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));
        if (product.getAvailable()) {
            if (paidInsurance(theId)) {
                if (product.getBuyNow() != 0) {
                    if (user.getBalance() >= product.getBuyNow() || product.getBuyNow() > 20000) {
                        if (product.getBuyNow() < 20000) {
                            if (IsInRoom(theId)) {
                                BidOnProduct bidOnProduct = findInRoom(product.getId());
                                User oldUser = bidOnProduct.getUserId();
                                oldUser.setBalance((long) (oldUser.getBalance() + bidOnProduct.getHighestBid()));
                                roomRepo.delete(bidOnProduct);
                            }
                            user.setBalance((long) (user.getBalance() - product.getBuyNow()));
                            seller.setBalance((long) (seller.getBalance() + product.getBuyNow() + product.getInsuranceAmount()));
                            user.setBuying(user.getBuying() + 1);
                            seller.setSelling(seller.getSelling() + 1);

                            if (user.getBuying()>=5 &&!flag) {
                                user.setVerified(true);
                            }
                            if (seller.getSelling()>=5 &&!flag) {
                                seller.setVerified(true);
                            }

                        }

                        product.setAvailable(false);
                        product.setBuyerId(user.getId());
                        List<User> productUsers = product.users;
                        for (User productUser : productUsers) {
                            if (ifThisUserPaidInsurance(product.getId(), productUser.getId())) {
                                if (!productUser.getId().equals(user.getId())) {
                                    productUser.setBalance(productUser.getBalance() + (long) product.getInsuranceAmount());
                                }
                            }
                        }
                        if (product.getBuyNow() > 20000) {
                            seller.setBalance((long) (seller.getBalance() + product.getInsuranceAmount()));
                            product.setProcessing(true);
                            user.setBuying(user.getBuying() + 1);
                            seller.setSelling(seller.getSelling() + 1);

                            if (user.getBuying()>=5 &&!flag) {
                                user.setVerified(true);
                            }
                            if (seller.getSelling()>=5 &&!flag) {
                                seller.setVerified(true);
                            }

                        }



                        if (user.getBuying() % 5 == 0) {
                            user.setBalance(user.getBalance() + 1000);
                        }
                        if (seller.getSelling() % 5 == 0) {
                            seller.setBalance(seller.getBalance() + 1000);
                        }
                        productUsers.clear();
                        productRepo.save(product);
                        return ResponseEntity.ok().body("success");
                    } else
                        return ResponseEntity.ok().body("Your Balance Is Not Enough :(");
                } else
                    return ResponseEntity.ok().body("The Product Can't Be Bought With BuyNow :(");
            } else
                return ResponseEntity.ok().body("You Have To Pay The Insurance First :( ");
        } else
            return ResponseEntity.ok().body("The Product Isn't Available :(");

    }

    // table Bids On Product
    // start biding
    @Override
    public ResponseEntity<?> addToRoom(Product product, float newPrice) {
        Date date = new Date();
        if (!IsInRoom(product.getId())) {
            if (paidInsurance(product.getId())) {
                if (newPrice >= product.getStartPrice()) {
                    BidOnProduct bidOnProduct = new BidOnProduct();
                    String email = SecurityContextHolder.getContext().getAuthentication().getName();
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));

                    if (product.getBuyNow() < 20000) {
                        user.setBalance((long) (user.getBalance() - newPrice));
                    }
                    bidOnProduct.setUserId(user);
                    bidOnProduct.setProductId(product);
                    bidOnProduct.setStartDate(product.getStartDate());
                    bidOnProduct.setEndDate(product.getEndDate());
                    bidOnProduct.setHighestBid(newPrice);
                    bidOnProduct.setBidingDate(date);
                    product.setHighestPrice(newPrice);
                    saveRoom(bidOnProduct);

                    return ResponseEntity.ok().body("success");
                } else
                    return ResponseEntity.ok().body("You Have To Pay As StartPrice Or Higher :(");
            } else
                return ResponseEntity.ok().body("You Have To Pay The Insurance First :(");
        } else {
            ResponseEntity<?> responseEntity = updateRoom(product, newPrice);
            return ResponseEntity.ok().body("Product Updated :)");
        }
    }


    @Override
    public BidOnProduct saveRoom(BidOnProduct bidOnProduct) {
        return roomRepo.save(bidOnProduct);
    }

    @Override
    public ResponseEntity<?> updateRoom(Product product, float newPrice) {
        BidOnProduct bidOnProduct = findInRoom(product.getId());
        if (product.getAvailable()) {
            if (newPrice > bidOnProduct.getHighestBid()) {
                if (newPrice >= product.getIncrementbid()) {
                    String email = SecurityContextHolder.getContext().getAuthentication().getName();
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Please provide a valid Email!"));
                    if (product.getBuyNow() < 20000) {
                        User oldUser = bidOnProduct.getUserId();
                        oldUser.setBalance(oldUser.getBalance() + (long) bidOnProduct.getHighestBid());
                        userRepository.save(oldUser);
                        user.setBalance(user.getBalance() - (long) newPrice);
                        userRepository.save(user);
                    }
                    roomRepo.delete(bidOnProduct);
                    BidOnProduct newBid = new BidOnProduct();
                    newBid.setUserId(user);
                    newBid.setProductId(product);
                    newBid.setStartDate(product.getStartDate());
                    newBid.setEndDate(product.getEndDate());
                    newBid.setHighestBid(newPrice);
                    newBid.setBidingDate(new Date());
                    product.setHighestPrice(newPrice);
                    roomRepo.save(newBid);
                    return ResponseEntity.ok().body("success");

                }
                return ResponseEntity.ok().body("You Have To Bid as Increment Bid Or Higher :(");
            }
            return ResponseEntity.ok().body("Add Higher Price :(");
        }
        return ResponseEntity.ok().body("Product Isn't Available");
    }


    @Override
    public List<RoomResponse> findAllBidOnProduct() {
        List<BidOnProduct> bidOnProducts = roomRepo.findAll();
        List<ProductBiderId> IDs = new ArrayList<>();
        Date date = null;
        for (BidOnProduct bidOnProduct : bidOnProducts) {
            IDs.add(bidOnProduct.getId());
            date = bidOnProduct.getBidingDate();
        }

        List<RoomResponse> roomResponses = new ArrayList<>();

        for (ProductBiderId productBiderId : IDs) {

            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setProductID(String.valueOf(productBiderId.getProductId()));
            roomResponse.setUserID(String.valueOf(productBiderId.getUserId()));
            Product product = findById(productBiderId.getProductId());

            roomResponse.setStartDate(String.valueOf(product.getStartDate()));
            roomResponse.setEndDate(String.valueOf(product.getEndDate()));
            roomResponse.setHighestPrice(product.getHighestPrice());
            roomResponse.setBiddingDate(String.valueOf(date));
            Optional<User> user = userRepository.findById(productBiderId.getUserId());
            roomResponse.setUserName(user.get().getName());
            roomResponses.add(roomResponse);
        }
        return roomResponses;
    }

    @Override
    public Boolean IsInRoom(long theId) {
        boolean isInRoom = false;
        List<BidOnProduct> bidOnProducts = roomRepo.findAll();
        for (BidOnProduct bidOnProduct : bidOnProducts) {
            if (bidOnProduct.getProductId().getId().equals(theId)) {
                isInRoom = true;
                break;
            }
        }
        return isInRoom;
    }

    @Override
    public RoomResponse findRoomWithID(long theId) {
        List<BidOnProduct> bidOnProducts = roomRepo.findAll();
        ProductBiderId IDs = new ProductBiderId();
        Date date = null;
        float heighestPrice = 0;

        for (BidOnProduct bidOnProduct : bidOnProducts) {
            if (bidOnProduct.getProductId().getId().equals(theId)) {
                IDs.setProductId(bidOnProduct.getProductId().getId());
                IDs.setUserId(bidOnProduct.getId().getUserId());
                date = bidOnProduct.getBidingDate();
                heighestPrice = bidOnProduct.getHighestBid();
            }
        }

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setProductID(String.valueOf(IDs.getProductId()));
        roomResponse.setUserID(String.valueOf(IDs.getUserId()));

        Product product = findById(theId);
        roomResponse.setStartDate(String.valueOf(product.getStartDate()));
        roomResponse.setEndDate(String.valueOf(product.getEndDate()));
        roomResponse.setHighestPrice(heighestPrice);
        roomResponse.setBiddingDate(String.valueOf(date));
        Optional<User> user = userRepository.findById(IDs.getUserId());
        roomResponse.setUserName(user.get().getName());
        return roomResponse;
    }

    @Override
    public BidOnProduct findInRoom(long productId) {
        List<BidOnProduct> bidOnProducts = roomRepo.findAll();
        for (BidOnProduct bidOnProduct : bidOnProducts) {
            if (bidOnProduct.getProductId().getId().equals(productId)) {
                return bidOnProduct;
            }
        }
        return null;
    }

    @Override
    public void deleteRoom(BidOnProduct bidOnProduct) {
        roomRepo.delete(bidOnProduct);
    }

}
