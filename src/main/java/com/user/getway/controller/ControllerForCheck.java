package com.user.getway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ControllerForCheck {

	@GetMapping("/getdata")
	// @PreAuthorize("hasAuthority('ROLE_USER')")
	public String getdata() {
		return "user role";
	}

	@GetMapping("/nn")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String ggg() {
		// System.out.print(this.infoservice.getuserdata("hhyadav"));
		return "admin role";
	}

	@GetMapping("/bhai")
	public String mmm() {
		return "bhai";
	}
}
