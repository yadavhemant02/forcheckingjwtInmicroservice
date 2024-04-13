package com.user.getway.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.getway.entity.LoginOtp;
import com.user.getway.entity.UserIdentity;
import com.user.getway.helper.JwtHelper;
import com.user.getway.model.LoginRequest;
import com.user.getway.model.LoginWithOtpRequest;
import com.user.getway.model.RegisterRequest;
import com.user.getway.repository.LoginOtpRepository;
import com.user.getway.responsemodel.JwtResponse;
import com.user.getway.responsemodel.LoginResponse;
import com.user.getway.service.UserIdentityService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@Slf4j
public class UserIdentityController {

	private UserIdentityService userIdentityService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private LoginOtpRepository loginOtpRepo;

	@Autowired
	private JwtHelper jwtHelper;

	public UserIdentityController(UserIdentityService userIdentityService) {
		super();
		this.userIdentityService = userIdentityService;
	}

	@PostMapping("/user/registation")
	public ResponseEntity<Object> registerUserInfo(@RequestBody RegisterRequest userRegisterData) {

		try {
			UserIdentity responseData = this.userIdentityService.registerUserData(userRegisterData);
			if (responseData == null) {
				log.error("Error | data not insert in databases !");
				throw new NullPointerException("opps! insernal error for registation of user ");
			}

			log.info("message | user Register Successfully !");
			return new ResponseEntity<>("data inserted successflly !", HttpStatus.OK);

		} catch (Exception e) {
			log.error("Error | data not insert !");
			return new ResponseEntity<>("data not inserted !", HttpStatus.BAD_GATEWAY);
		}

	}

	@PostMapping("/user/login")
	public ResponseEntity<Object> loginUser(@RequestBody LoginRequest userLoginData) {

		try {
			LoginResponse responseData = this.userIdentityService.customerLogin(userLoginData);
			if (responseData == null) {
				log.error("Error | data not insert !");
				throw new NullPointerException("opps! insernal error for registation of user");
			}

			log.info("message | user Register Successfully !");
			return new ResponseEntity<>(responseData, HttpStatus.OK);

		} catch (Exception e) {
			log.error("Error | data not insert !");
			return new ResponseEntity<>("data not inserted !", HttpStatus.BAD_GATEWAY);
		}
	}

	@PostMapping("/user/login-with-otp")
	public ResponseEntity<Object> userLoginWithOtp(@RequestBody LoginWithOtpRequest loginOtpData) {

		boolean check = this.userIdentityService.checkOtp(loginOtpData);

		LoginOtp otpResponse = this.loginOtpRepo.findByUserOtp(loginOtpData.getUserOtp());
		long noOfSeconds = otpResponse.getCreatedAt().until(LocalDateTime.now(), ChronoUnit.SECONDS);
		long min = noOfSeconds / 60;

		if (!check) {
			log.warn("Warm! | otp have been wrong.");
			return new ResponseEntity<>("bad", HttpStatus.BAD_GATEWAY);
		} else if (min > 2) {
			log.warn("Warm! | otp expaired.!");
			return new ResponseEntity<>("bad", HttpStatus.BAD_GATEWAY);
		}

		try {

			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginOtpData.getUserEmail(), loginOtpData.getUserPassword()));

			if (authentication.isAuthenticated()) {
				UserDetails userDetailseData = this.userIdentityService.loadUserByUsername(loginOtpData.getUserEmail());

				String token = this.jwtHelper.generateToken(userDetailseData);

				String encodedToken = Base64.getEncoder().encodeToString(token.getBytes());

				JwtResponse jwtResponseData = new JwtResponse();
				jwtResponseData.setToken(encodedToken);
				jwtResponseData.setUserEmail(loginOtpData.getUserEmail());
				jwtResponseData.setStatus("success");
				log.info("Massage | right customer login credencial!.");
				return new ResponseEntity<>(jwtResponseData, HttpStatus.OK);
			} else {
				log.warn("Warn | customer not authenticate!");
				return new ResponseEntity<>("not authenticate", HttpStatus.BAD_GATEWAY);
			}
		} catch (Exception e) {
			log.error("Error! | Internal server Error related to database !");
			return new ResponseEntity<>("bad", HttpStatus.BAD_GATEWAY);
		}

	}

}
