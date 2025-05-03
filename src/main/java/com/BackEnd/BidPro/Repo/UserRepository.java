package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phone")
    Optional<User> findByPhone(@Param("phone")String phone);
    @Query("SELECT u FROM User u WHERE u.nationalId = :nationalId")
    Optional<User> findByNationalId(@Param("nationalId")String nationalId);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);




    Optional<User> findByVerificationToken(String token);
}
