package com.user.getway.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.getway.entity.LoginOtp;
import com.user.getway.entity.UserIdentity;
import com.user.getway.model.LoginRequest;
import com.user.getway.model.LoginWithOtpRequest;
import com.user.getway.model.RegisterRequest;
import com.user.getway.repository.LoginOtpRepository;
import com.user.getway.repository.userIdentityRepository;
import com.user.getway.responsemodel.LoginResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ComponentScan
@Service
@Slf4j
public class UserIdentityService implements UserDetailsService {

	private userIdentityRepository userIdentityRepo;

	private PasswordEncoder passwordEncoded;

	private LoginOtpRepository loginOtpRepo;

	public UserIdentityService(userIdentityRepository userIdentityRepo, PasswordEncoder passwordEncoded,
			LoginOtpRepository loginOtpRepo) {
		super();
		this.userIdentityRepo = userIdentityRepo;
		this.passwordEncoded = passwordEncoded;
		this.loginOtpRepo = loginOtpRepo;
	}

	public UserIdentity registerUserData(RegisterRequest userData) {

		UserIdentity entityData = new UserIdentity();

		try {
			entityData.setUserName(userData.getUserName());
			entityData.setUserEmail(userData.getUserEmail());
			entityData.setUserPassword(passwordEncoded.encode(userData.getUserPassword()));
			entityData.setUserCode(UUID.randomUUID().toString().replace("-", "").substring(12));
			entityData.setUserRole(userData.getUserRole());
			entityData.setUserCreated(LocalDateTime.now());
			UserIdentity responseData = this.userIdentityRepo.save(entityData);

			if (responseData == null) {
				log.error("Error | user not registerd !");
				throw new IllegalAccessException("Opps! user not registerd..! ");
			}

			log.info("Message | user register Successfully !");
			return responseData;

		} catch (Exception e) {
			log.error("Error | user not register Successfully in database !");
			throw new DataRetrievalFailureException("Failed to insert data" + e.getMessage());
		}

	}

	@Transactional(rollbackOn = RuntimeException.class)
	public LoginResponse customerLogin(LoginRequest loginData) {

		LoginResponse responseData = new LoginResponse();

		try {

			UserIdentity entityData = this.userIdentityRepo.findByUserEmail(loginData.getUserEmail());

			if (!this.passwordEncoded.matches(loginData.getUserPassword(), entityData.getUserPassword())) {
				log.error("Error | password not match");
				throw new IllegalArgumentException("wrong credential");
			}

			int otp = generateOtp();

			LoginOtp otpResponse = new LoginOtp();
			LoginOtp otpEntity = null;

			LoginOtp otpData = this.loginOtpRepo.findByUserCode(entityData.getUserCode());
			if (otpData != null) {
				otpData.setCreatedAt(LocalDateTime.now());
				otpData.setUserOtp(otp);
				otpEntity = this.loginOtpRepo.save(otpData);

			} else {

				otpResponse.setUserOtp(otp);
				otpResponse.setUserEmail(loginData.getUserEmail());
				otpResponse.setCreatedAt(LocalDateTime.now());
				otpResponse.setUserCode(entityData.getUserCode());
				otpEntity = this.loginOtpRepo.save(otpResponse);
			}

			if (otpEntity.getId() == null) {
				log.error("Error | otp  not generated !");
				throw new IllegalArgumentException("Customer Not Inserted !!");
			}

			responseData.setUserEmail(entityData.getUserEmail());
			responseData.setUserCode(entityData.getUserCode());
			responseData.setOtp(otp);
			responseData.setStatus("Right Credencial !!");
			log.info("Massege | Otp generated");
			return responseData;

		} catch (Exception e) {
			log.error("Error | otp  not generated database error");
			throw new DataRetrievalFailureException("Failed to retrieve data" + e.getMessage());
		}
	}

	public int generateOtp() {

		String otpNumber = Integer.toString(((int) (Math.random() * 10000)));
		if (otpNumber.length() < 4) {
			otpNumber += "0";
		}
		return Integer.parseInt(otpNumber);
	}

	@SuppressWarnings("null")
	public boolean checkOtp(LoginWithOtpRequest data) {

		LoginOtp loginOtpResponse = this.loginOtpRepo.findByUserOtp(data.getUserOtp());

		if (loginOtpResponse == null && loginOtpResponse.getUserEmail() != data.getUserEmail()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails responseData = this.userIdentityRepo.findByUserEmail(username);
		return responseData;
	}

}
