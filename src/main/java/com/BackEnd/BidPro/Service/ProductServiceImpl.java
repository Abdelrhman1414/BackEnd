package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.BidOnProduct;
import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return productResponses1;
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
        productRepo.save(product);
        Notification notification = new Notification();
        notification.setMessage(user.getName() + " your product "+productRequest.getTitle()+" added successfully!");
        notificationService.sendNotification(String.valueOf(user.getId()), notification);
        notificationRepository.save(notification);


    }


    @Override
    public void deleteById(Long theId) {
        productRepo.deleteById(theId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ResponseEntity<?> insuranceAmountHandling(long theId) {
        Product product = findById(theId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));


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

        if (userBalance > productAmount) {
            user.setBalance((long) (userBalance - productAmount));
            product.addUser(user);
            save(product);
            addToRoom(theId);

            return ResponseEntity.ok().body("Added insurance table successfully :)");
        } else

            return ResponseEntity.ok().body("Your balance is not enough :(");
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
    public ResponseEntity<?> buyingWithBuyNow(long theId) {
        Product product = findById(theId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));

        Long userBalance = user.getBalance();

        if (product.getAvailable()) {
            if (paidInsurance(theId)) {
                if (product.getBuyNow() != 0) {
                    if (userBalance >= product.getBuyNow()) {
                        user.setBalance((long) (userBalance - product.getBuyNow()));
                        deleteById(theId);
                        return ResponseEntity.ok().body("You Have bought The Product Successfully:)");
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
    public void addToRoom(long theId) {
        Product product = findById(theId);

        if (paidInsurance(theId)) {

            BidOnProduct bidOnProduct = new BidOnProduct();
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
            long userId = user.getId();
            bidOnProduct.setUserId(user);
            bidOnProduct.setProductId(product);


            bidOnProduct.setStartDate(product.getStartDate());

            bidOnProduct.setEndDate(product.getEndDate());

            bidOnProduct.setHighestBid(product.getHighestPrice());

            saveRoom(bidOnProduct);

        }
    }

    public BidOnProduct saveRoom(BidOnProduct bidOnProduct) {
        return roomRepo.save(bidOnProduct);
    }

    public List<BidOnProduct> findAllBidOnProduct() {
        return roomRepo.findAll();
    }

    public void delete(BidOnProduct bidOnProduct) {
        roomRepo.delete(bidOnProduct);
    }

}
