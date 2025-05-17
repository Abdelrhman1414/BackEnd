package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.UserRequest;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Dto.Response.UserResponse;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.cloudinary.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            if(user.getRole().name().equals("USER")) {
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setName(user.getName());
                userResponse.setEmail(user.getEmail());
                userResponse.setNationalid(user.getNationalId());
                userResponse.setPhonenumber(user.getPhoneNumber());
                userResponse.setGovernorate(user.getGovernorate());
                userResponse.setCity(user.getCity());
                userResponse.setAddress(user.getAddress());
                userResponse.setVerified(user.isVerified());
                userResponses.add(userResponse);
            }
        }
        return userResponses;
    }

    @Override
    public void deleteUser(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("Something Wrong Happened, Please Try Again!"));
        userRepository.delete(user);
    }


    @Override
    public UserResponse details() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        if (user.getImage() == null) {
            userResponse.setImage_url(null);
        } else {
            userResponse.setImage_url(user.getImage().getUrl());
        }
        userResponse.setNationalid(user.getNationalId());
        userResponse.setPhonenumber(user.getPhoneNumber());
        userResponse.setGovernorate(user.getGovernorate());
        userResponse.setCity(user.getCity());
        userResponse.setAddress(user.getAddress());
        userResponse.setVerified(user.isVerified());


        return userResponse;
    }

    @Override
    public void edit(UserRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));

        if (request.getName()!=null){
            user.setName(request.getName());
            System.out.println(user.getName());
        }

        if (request.getPhonenumber()!=null){
            user.setPhoneNumber(request.getPhonenumber());
        }

        if (request.getGovernorate()!=null){
            user.setGovernorate(request.getGovernorate());
        }
        if (request.getCity()!=null){
            user.setCity(request.getCity());
        }

        if (request.getAddress()!=null){
            user.setAddress(request.getAddress());
        }

        userRepository.save(user);
    }


    @Override
    public boolean verifyEmail(String token){
        User user = userRepository.findByVerificationToken(token).orElseThrow(()->new RuntimeException("Please provide an valid email!"));
        if (user != null && !user.isEmailVerified()) {
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }





    @Transactional
    @Override
    public List<ProductResponse> userAdvertisements() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        List<Product> products = user.getSellerProducts();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            if(product.getAvailable()){
            ProductResponse productResponse = new ProductResponse();
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
            productResponse.setID(String.valueOf(product.getId()));

            productResponse.setSellerName(product.getSeller().getName());
            productResponse.setPending(product.getIsPending());
            List<String> urls = new ArrayList<>();
            for(Image image : product.getImages()){
                urls.add(image.getUrl());
            }
            productResponse.setUrls(urls);
            productResponses.add(productResponse);
        }
        }
        return productResponses;
    }


}
