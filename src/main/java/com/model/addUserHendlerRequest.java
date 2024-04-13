package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class addUserHendlerRequest {

	private String userName;
	private String userEmail;
	private String userMobileNumber;
	private String userPassword;
	private String userImage;

}
