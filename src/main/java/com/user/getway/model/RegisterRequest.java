package com.user.getway.model;

import lombok.Data;

@Data
public class RegisterRequest {
	
	private String userName;
	private String userEmail;
	private String userPassword;
	private String userRole;

}
