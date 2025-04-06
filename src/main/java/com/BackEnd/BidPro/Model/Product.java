package com.BackEnd.BidPro.Model;

import com.BackEnd.BidPro.cloudinary.model.Image;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "highest_bid")
    private float highestPrice;

    @Column(name = "increment_bid")
    private float incrementbid;

    @Column(name = "buy_now")
    private float buyNow;

    @Column(name = "start_price")
    private float startPrice;

    @Column(name = "title")
    private String title;

    @Column(name = "insurance_amount")
    private float insuranceAmount;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    private long BuyerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_pending")
    private Boolean isPending;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "processing")
    private Boolean processing;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "insurance",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public List<User> users;

//    @OneToMany(mappedBy = "productId")
//    private Set<BidOnProduct> bidOnProducts = new HashSet<>();


    public void addUser(User theUser) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(theUser);
    }




}
