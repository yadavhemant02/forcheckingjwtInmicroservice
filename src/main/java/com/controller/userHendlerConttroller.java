package com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.userHendler;
import com.model.LoginUserHendlerRequest;
import com.model.LoginUserHendlerResponse;
import com.model.addUserHendlerRequest;
import com.model.addUserHendlerResponse;
import com.service.userHendlerService;

@RestController
@RequestMapping("/user")
public class userHendlerConttroller {

	@Autowired
	private userHendlerService userService;

	@PostMapping("/add/userData")
	public ResponseEntity<?> adduserData(@RequestBody addUserHendlerRequest data) {

		try {
			addUserHendlerResponse ResponseData = this.userService.addUserHendler(data);
			if (ResponseData.getUserImage() == null) {
				return new ResponseEntity<addUserHendlerResponse>(ResponseData, HttpStatus.BAD_GATEWAY);
			}

			return new ResponseEntity<addUserHendlerResponse>(ResponseData, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addUserHendlerResponse userResponceData = new addUserHendlerResponse();
			userResponceData.setUserName(data.getUserName());
			userResponceData.setUserImage(null);
			userResponceData.setRegisterStatus("Opps ! sorry Internals Error .!");
			return new ResponseEntity<addUserHendlerResponse>(userResponceData, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Login/userData")
	public ResponseEntity<?> LoginUserData(@RequestBody LoginUserHendlerRequest data) {
		System.out.print(data);
		try {
			LoginUserHendlerResponse loginResponse = this.userService.loginUserHendeler(data);
			if (loginResponse.getUserImage() == null) {
				return new ResponseEntity<LoginUserHendlerResponse>(loginResponse, HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<LoginUserHendlerResponse>(loginResponse, HttpStatus.OK);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LoginUserHendlerResponse LoginUserResponse = new LoginUserHendlerResponse();
			LoginUserResponse.setUserName("Opps !Not Found.");
			LoginUserResponse.setUserImage(null);
			LoginUserResponse.setUserStatus("Bad Credentials..!");
			return new ResponseEntity<LoginUserHendlerResponse>(LoginUserResponse, HttpStatus.BAD_GATEWAY);
		}

	}

	@GetMapping("/get/{userCode}")
	public List<userHendler> getUserDetails(@PathVariable("userCode") String userCode) {
		List data = new ArrayList<>();
		userHendler dd = this.userService.getuserDetailsById(userCode);
		data.add(dd);
//		try {
//			userHendler userData = this.userService.getuserDetailsById(userCode);
//			if (userData == null) {
//				throw new NullPointerException("error_NoData");
//			}
//			return new ResponseEntity<userHendler>(userData, HttpStatus.OK);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_GATEWAY);
//		}
		return data;
	}

	@GetMapping("/kk")
	public String ss() {
		System.out.println("Massege | all is good ..!!");
		return "kk";
	}

}
