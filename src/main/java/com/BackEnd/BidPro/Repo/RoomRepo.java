package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.BidOnProduct;
import com.BackEnd.BidPro.Model.ProductBiderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepo extends JpaRepository<BidOnProduct, ProductBiderId> {

}
