package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phone")
    Optional<User> findByPhone(@Param("phone")String phone);
    @Query("SELECT u FROM User u WHERE u.nationalId = :nationalId")
    Optional<User> findByNationalId(@Param("nationalId")String nationalId);
}
