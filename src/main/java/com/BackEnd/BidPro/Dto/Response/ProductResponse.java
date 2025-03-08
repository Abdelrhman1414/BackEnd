package com.BackEnd.BidPro.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String description;
    private String startDate;
    private String endDate;
    private String incrementbid;
    private String buyNow;
    private String title;
    private String insuranceAmount;
    private String quantity;
    private String startPrice;
    private String url;
    private String sellerName;
    private String categoryName;
}
