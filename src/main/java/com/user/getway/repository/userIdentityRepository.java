package com.user.getway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.getway.entity.UserIdentity;

public interface userIdentityRepository extends JpaRepository<UserIdentity, Long> {

	UserIdentity findByUserEmail(String userEmail);

}
