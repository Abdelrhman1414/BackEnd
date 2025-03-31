package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.InterestsRequest;
import com.BackEnd.BidPro.Dto.Response.InterestsResponse;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;

import java.util.List;


public interface InterestsService {
    void addInterests(InterestsRequest interestsRequest);
    List<InterestsResponse> findInterestCategory();
    List<ProductResponse> findProductInterests();
}
