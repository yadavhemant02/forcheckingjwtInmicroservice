package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.userHendler;

public interface userHendlerRepository extends JpaRepository<userHendler, Long> {

	userHendler findByUserMobileNumber(String userMobileNumber);

	userHendler findByUserEmail(String userMobileNumber);

	userHendler findByUserCode(String userCode);

}
