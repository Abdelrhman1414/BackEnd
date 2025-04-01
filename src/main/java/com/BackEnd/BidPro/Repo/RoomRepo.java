package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.BidOnProduct;
import com.BackEnd.BidPro.Model.ProductBiderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository <BidOnProduct, ProductBiderId> {
}
