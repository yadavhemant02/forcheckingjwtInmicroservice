package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class userHendler {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	private String userName;
	@Column(unique = true)
	private String userEmail;
	@Column(unique = true)
	private String userMobileNumber;
	private String userPassword;
	private byte[] userImage;
	private String userCode;

}
