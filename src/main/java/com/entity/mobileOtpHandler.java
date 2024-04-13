package com.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class mobileOtpHandler {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Long;
	private String userCode;
	private String userOtp;
	private LocalDateTime imageDate;

}
