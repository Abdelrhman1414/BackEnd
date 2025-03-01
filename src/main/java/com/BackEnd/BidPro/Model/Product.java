package com.BackEnd.BidPro.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "highest_bid")
    private float highestPrice;

    @Column(name = "increment_bid")
    private float incrementbid;

    @Column(name = "buy_now")
    private float buyNow;

    @Column(name = "title")
    private String title;

    @Column(name = "insurance_amount")
    private float insuranceAmount;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "seller_id")
    private int sellerId;



//    @ManyToOne(fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                       CascadeType.REFRESH, CascadeType.DETACH})
//    @JoinColumn(name = "category_id")
//     private Category category;

    @Column(name = "is_pending")
    private Boolean isPending;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "processing")
    private Boolean processing;
}
