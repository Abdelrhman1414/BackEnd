package com.BackEnd.BidPro.Dto.Request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private List<MultipartFile> files;
    private String description;
    private String startDate;
    private String endDate;
    private String incrementbid;
    private String buyNow;
    private String title;
    private String insuranceAmount;
    private String quantity;
}