package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.CategoryRequest;
import com.BackEnd.BidPro.Dto.Response.CategoryResponse;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Model.Category;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.CategoryRepo;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.cloudinary.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepo categoryRepo;
    private final UserRepository userRepository;


    @Autowired
    public CategoryServiceImpl(CategoryRepo theCategoryRepo, UserRepository theUserRepository) {
        categoryRepo = theCategoryRepo;
        userRepository = theUserRepository;
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categoryList = categoryRepo.findAll();
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setName(category.getName());
            categoryResponseList.add(categoryResponse);
        }
        return categoryResponseList;
    }

    @Override
    public CategoryResponse findById(Long theId) {
        Optional<Category> result = categoryRepo.findById(theId);
        CategoryResponse categoryResponse = new CategoryResponse();

        Category category = null;
        if (result.isPresent()) {
            category = result.get();
        } else {
            throw new RuntimeException("Did not find product id - " + theId);
        }
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }

    @Override
    public CategoryResponse findCategoryAndProductsByCategoryId(Long theId) {
        Optional<Category> result = categoryRepo.findById(theId);
        CategoryResponse categoryResponse = new CategoryResponse();
        List<ProductResponse> productResponseList = new ArrayList<>();


        Category category = null;
        if (result.isPresent()) {
            category = result.get();
        } else {
            throw new RuntimeException("Did not find product id - " + theId);
        }

        for (Product product : category.getProducts()) {
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

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
            productResponse.setSellerName(user.getName());
            List<String> urls = new ArrayList<>();
            for(Image image : product.getImages()){
                urls.add(image.getUrl());
            }
            productResponse.setUrls(urls);

            productResponseList.add(productResponse);
        }
        categoryResponse.setProducts(productResponseList);
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }

    @Override
    public void addCategory(CategoryRequest categoryRequest) {
//        try {
//            Category category = new Category();
//            category.setName(categoryRequest.getName());
//            categoryRepo.save(category);
//            return ResponseEntity.ok().body("Added Category successfully!");
//
//        } catch (Exception e) {
//
//            return null;
//        }
        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepo.save(category);

    }

    @Override
    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(Long id)
    {
        Category category=categoryRepo.findById(id).orElseThrow(()->new RuntimeException("Something Wrong Happened, Please Try Again!"));
        categoryRepo.delete(category);
    }
}
