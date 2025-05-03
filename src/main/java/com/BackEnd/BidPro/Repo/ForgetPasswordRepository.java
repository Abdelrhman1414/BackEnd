package com.BackEnd.BidPro.Repo;

import com.BackEnd.BidPro.Model.ForgetPassword;
import com.BackEnd.BidPro.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {
    @Query("select fp from ForgetPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgetPassword> findByOtpAndUser(String otp, User user);
}
