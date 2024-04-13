package com.user.getway.responsemodel;

import lombok.Data;

@Data
public class JwtResponse {

	private String token;
	private String userEmail;
	private String status;

}
