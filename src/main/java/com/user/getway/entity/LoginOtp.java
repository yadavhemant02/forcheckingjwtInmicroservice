package com.user.getway.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Login_otp")
public class LoginOtp {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_otp")
	@SequenceGenerator(sequenceName = "login", name = "login_otp")
	@Column(name = "id")
	private Long id;

	@Column(name = "user_otp")
	private int userOtp;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "user_code")
	private String userCode;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

}
