package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
