package com.service;

import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.entity.mobileOtpHandler;
import com.entity.userHendler;
import com.model.LoginUserHendlerRequest;
import com.model.LoginUserHendlerResponse;
import com.model.addUserHendlerRequest;
import com.model.addUserHendlerResponse;
import com.repository.MobileOtpHandlerRepository;
import com.repository.userHendlerRepository;

@Service
public class userHendlerService {

	@Autowired
	private PasswordEncoder passwordEncode;

	@Autowired
	private JavaMailSender mailsender;

	@Autowired
	private MobileOtpHandlerRepository mobileOtpRepo;

	@Autowired
	private userHendlerRepository userRepository;

	public addUserHendlerResponse addUserHendler(addUserHendlerRequest data) {

		if (this.userRepository.findByUserMobileNumber(data.getUserMobileNumber()) != null) {
			addUserHendlerResponse userResponceData = new addUserHendlerResponse();
			userResponceData.setUserName(data.getUserName());
			userResponceData.setUserImage(null);
			userResponceData.setRegisterStatus("Opps ! your Mobile Number Already Exist .!");
			return userResponceData;
		}

		if (this.userRepository.findByUserEmail(data.getUserEmail()) != null) {
			addUserHendlerResponse userResponceData = new addUserHendlerResponse();
			userResponceData.setUserName(data.getUserName());
			userResponceData.setUserImage(null);
			userResponceData.setRegisterStatus("Opps ! your Email Already Exist .!");
			return userResponceData;
		}

		// changeing data model to entity or base62->>byte[]
		userHendler entityData = new userHendler();
		entityData.setUserName(data.getUserName());
		entityData.setUserEmail(data.getUserEmail());
		entityData.setUserPassword(data.getUserPassword());
		entityData.setUserCode(usingMath());
		entityData.setUserMobileNumber(data.getUserMobileNumber());

		// base-->>byte[]
		entityData.setUserImage(base64ToByteImage(data.getUserImage()));

		try {
			userHendler userHendlerData = this.userRepository.save(entityData);
			if (userHendlerData == null) {
				throw new NullPointerException("error_NoData");
			} else {

				addUserHendlerResponse userResponceData = new addUserHendlerResponse();
				userResponceData.setUserName(userHendlerData.getUserName());
				userResponceData.setRegisterStatus("Successfuly Inserted data!");
				userResponceData.setUserImage(byteImageToBase64(userHendlerData.getUserImage()));
				return userResponceData;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addUserHendlerResponse userResponceData = new addUserHendlerResponse();
			userResponceData.setUserName(data.getUserName());
			userResponceData.setUserImage(null);
			userResponceData.setRegisterStatus("Opps ! sorry Internals Error .!");
			return userResponceData;
		}
	}

	// it is for generate Random UserCode..
	static String usingMath() {
		String alphabetsInUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		// create a super set of all characters
		String allCharacters = alphabetsInLowerCase + alphabetsInUpperCase + numbers;
		// initialize a string to hold result
		StringBuffer randomString = new StringBuffer();
		// loop for 10 times
		for (int i = 0; i < 10; i++) {
			// generate a random number between 0 and length of all characters
			int randomIndex = (int) (Math.random() * allCharacters.length());
			// retrieve character at index and add it to result
			randomString.append(allCharacters.charAt(randomIndex));
		}
		return randomString.toString();
	}

	public static byte[] base64ToByteImage(String base64String) {
		byte[] imageBytes = Base64.getDecoder().decode(base64String);
		return imageBytes;
	}

	public static String byteImageToBase64(byte[] imageBytes) {
		// Encode the byte array to Base64
		String base64String = Base64.getEncoder().encodeToString(imageBytes);
		return base64String;
	}

	public LoginUserHendlerResponse loginUserHendeler(LoginUserHendlerRequest data) {

		boolean atleastOneAlpha = data.getUserLoginWithField().matches(".*[a-zA-Z]+.*");

		try {

			if (atleastOneAlpha) {
				userHendler userEntityData = this.userRepository.findByUserEmail(data.getUserLoginWithField());
				if (userEntityData == null) {
					throw new NullPointerException("error_NoData");
				} else {
					LoginUserHendlerResponse LoginUserResponse = new LoginUserHendlerResponse();
					LoginUserResponse.setUserName(userEntityData.getUserName());
					LoginUserResponse.setUserImage(byteImageToBase64(userEntityData.getUserImage()));
					LoginUserResponse.setUserStatus("Right Credentials..!");

					// for otp
					String otp = EmailOtpGenerated(userEntityData.getUserEmail());
					String bycyptOtpData = this.passwordEncode.encode(otp);
					LoginUserResponse.setUserOtp(bycyptOtpData);

					return LoginUserResponse;
				}
			} else {
				userHendler userEntityData = this.userRepository.findByUserMobileNumber(data.getUserLoginWithField());
				if (userEntityData == null) {
					throw new NullPointerException("error_NoData");
				} else {
					LoginUserHendlerResponse LoginUserResponse = new LoginUserHendlerResponse();

					LoginUserResponse.setUserName(userEntityData.getUserName());
					LoginUserResponse.setUserImage(byteImageToBase64(userEntityData.getUserImage()));
					LoginUserResponse.setUserStatus("Right Credentials..!");
					String otp = Integer.toString((int) (Math.random() * 9000) + 1000);
					LoginUserResponse.setUserOtp(otp);

					// for inserting object of OTP ...
					mobileOtpHandler otpObject = new mobileOtpHandler();
					LocalDateTime currentDateTime = LocalDateTime.now();
					otpObject.setImageDate(currentDateTime);
					otpObject.setUserCode(userEntityData.getUserCode());
					otpObject.setUserOtp(otp);
					this.mobileOtpRepo.save(otpObject);
					return LoginUserResponse;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LoginUserHendlerResponse LoginUserResponse = new LoginUserHendlerResponse();
			LoginUserResponse.setUserName("Opps !Not Found.");
			LoginUserResponse.setUserImage(null);
			LoginUserResponse.setUserStatus("Bad Credentials..!");
			return LoginUserResponse;
		}

	}

	public String EmailOtpGenerated(String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("yaadavhemant20056@gmail.com");
		message.setTo("yadavhemant9719@gmail.com");
		String otp = Integer.toString((int) (Math.random() * 9000) + 1000);
		message.setText("\r\n" + "Hi ,\r\n" + "\r\n"
				+ "We are sharing a onetime password to access the Request more details. The password is valid for 72 hours from the request. Please do not share it with anyone.\r\n"
				+ "\r\n" + "Username" + ":" + email + "\r\n" + "OTP" + ":" + otp);
		message.setSubject("OTP to access more details");
		mailsender.send(message);
		return otp;

	}

	public userHendler getuserDetailsById(String userCode) {
		userHendler userData = this.userRepository.findByUserCode(userCode);
		if (userData == null) {
			return null;
		} else {
			return userData;
		}
	}

}
