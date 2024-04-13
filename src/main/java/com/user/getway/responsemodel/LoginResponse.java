package com.user.getway.responsemodel;

import lombok.Data;

@Data
public class LoginResponse {

	private String userEmail;
	private String userCode;
	private int otp;
	private String status;

}
