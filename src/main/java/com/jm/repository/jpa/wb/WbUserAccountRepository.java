package com.jm.repository.jpa.wb;

import com.jm.repository.po.wb.WbUserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WbUserAccountRepository extends JpaRepository<WbUserAccount, Integer>{

    WbUserAccount findByUserIdAndAccountType(int userId, int accountType);


	List<WbUserAccount> findByUserId(Integer userId);
}
