package com.user.getway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.getway.entity.LoginOtp;

public interface LoginOtpRepository extends JpaRepository<LoginOtp, Long> {

	LoginOtp findByUserCode(String userCode);

	LoginOtp findByUserOtp(int userOtp);

}
