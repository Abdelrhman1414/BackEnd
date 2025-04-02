package com.BackEnd.BidPro.Model;

import com.BackEnd.BidPro.Domain.Role;
import com.BackEnd.BidPro.cloudinary.model.Image;
import jakarta.persistence.*;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String nationalId;

    @Column(nullable = false,unique = true)
    private  String phoneNumber;

    @Column(nullable = false,unique = true)
    private String email;

    private String password;


    @Column(nullable = false)
    private String governorate;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="image_id")
    private Image image;

    @OneToMany(mappedBy = "seller")
    private List<Product> sellerProducts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="user_interests",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private List<Category> categoryList;

//    @OneToMany(mappedBy = "userId")
//    private Set<BidOnProduct> bidOnProducts = new HashSet<>();

//
//    @Column(nullable = false)
    private Long balance;


    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "users")
    private List<Product> products;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;

    }
}
