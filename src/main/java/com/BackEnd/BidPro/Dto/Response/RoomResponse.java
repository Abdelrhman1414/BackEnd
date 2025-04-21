package com.BackEnd.BidPro.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String UserID;
    private String ProductID;
    private String startDate;
    private String endDate;
    private String BiddingDate;
    private float highestPrice;
    private String UserName;
}
