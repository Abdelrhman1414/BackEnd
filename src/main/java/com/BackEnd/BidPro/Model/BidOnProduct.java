package com.BackEnd.BidPro.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.BackEnd.BidPro.Repo.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Entity
@Table(name = "bidsOnProduct")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidOnProduct {


    @EmbeddedId
    private ProductBiderId id = new ProductBiderId();


    @ManyToOne
    @MapsId("userId")
    private User userId;


    @ManyToOne
    @MapsId("productId")
    private Product productId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "highest_bid")
    private float highestBid;

    @Column(name = "biding_date")
    private Date bidingDate;

}
