package com.BackEnd.BidPro.cloudinary.repo;

import com.BackEnd.BidPro.cloudinary.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}