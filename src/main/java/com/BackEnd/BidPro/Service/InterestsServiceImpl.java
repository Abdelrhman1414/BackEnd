package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.InterestsRequest;
import com.BackEnd.BidPro.Dto.Response.InterestsResponse;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.CategoryRepo;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.cloudinary.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestsServiceImpl implements InterestsService {
    private final UserRepository userRepository;
    private final CategoryRepo categoryRepository;


    @Override
    public void addInterests(InterestsRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        if(user.getCategoryList()!=null) user.setCategoryList(null);
        List<Category> categories = new ArrayList<>();
        for(Long id : request.getCategoryIds()) {
            categories.add(categoryRepository.findById(id).get());
        }

        user.setCategoryList(categories);
        userRepository.save(user);
    }

    @Override
    public List<InterestsResponse> findInterestCategory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        List<InterestsResponse> interestsResponses = new ArrayList<>();
        for(Category category : user.getCategoryList()) {
            InterestsResponse interestsResponse = new InterestsResponse();
            interestsResponse.setId(category.getId());
            interestsResponse.setName(category.getName());
            interestsResponses.add(interestsResponse);
        }
        return interestsResponses;
    }

    @Override
    public List<ProductResponse> findProductInterests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        List<Category> categories = user.getCategoryList();
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(Category category : categories) {
            products.addAll(category.getProducts());
        }
        for(Product product : products) {
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
            List<String> urls = new ArrayList<>();
            for(Image image : product.getImages()){
                urls.add(image.getUrl());
            }
            productResponse.setUrls(urls);
            productResponses.add(productResponse);
        }
        return productResponses;
    }
}
