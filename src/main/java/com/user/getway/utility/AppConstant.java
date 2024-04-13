package com.user.getway.utility;

import org.springframework.stereotype.Component;

@Component
public class AppConstant {

	public static final String[] allowedUrl = { "/v3/api-docs/**", "/api-docs.yaml", "/swagger-ui/**",
			"/swagger-resources/**", "/webjars/**" };

}
