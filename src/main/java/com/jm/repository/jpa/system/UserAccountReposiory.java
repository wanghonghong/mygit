package com.jm.repository.jpa.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.system.user.UserAccount;

public interface UserAccountReposiory  extends JpaRepository<UserAccount, Integer> {

	UserAccount findByUserIdAndAccountType(Integer userId,Integer accountType);
	
}
