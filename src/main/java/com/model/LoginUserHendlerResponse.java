package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginUserHendlerResponse {

	private String userName;
	private String userImage;
	private String userOtp;
	private String userStatus;

}
