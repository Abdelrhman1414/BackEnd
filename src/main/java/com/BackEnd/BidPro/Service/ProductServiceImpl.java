package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.ProductRequest;
import com.BackEnd.BidPro.Model.Product;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.ProductRepo;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.cloudinary.model.Image;
import com.BackEnd.BidPro.cloudinary.service.CloudinaryService;
import com.BackEnd.BidPro.cloudinary.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
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
    private final UserRepository userRepository;


    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
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
    @Transactional
    public ResponseEntity<?> addProduct(ProductRequest productRequest) {
        try {
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
            productRepo.save(product);
            return ResponseEntity.ok().body("Added product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(Long theId) {
        productRepo.deleteById(theId);
    }
}
