package com.BackEnd.BidPro.Dto.Response;

import com.BackEnd.BidPro.cloudinary.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String ID;
    private String description;
    private String startDate;
    private String endDate;
    private float highestPrice;
    private String incrementbid;
    private String buyNow;
    private String title;
    private String insuranceAmount;
    private String quantity;
    private String startPrice;
    private List<String> urls;
    private String sellerName;
    private String sellerId;
    private String BuyerID;
    private String categoryName;
    private boolean isPending;
    private Boolean UserBidOnThisProduct;
}
