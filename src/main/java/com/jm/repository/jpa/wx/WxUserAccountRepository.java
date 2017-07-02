package com.jm.repository.jpa.wx;

import com.jm.repository.po.wx.WxUserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WxUserAccountRepository extends JpaRepository<WxUserAccount, Integer>{

	WxUserAccount findByUserIdAndAccountType(int userId, int accountType);

    /**
     * 查询大于某一个余额的佣金账户
     * @param shopId
     * @param accountType
     * @param balance
     * @return
     */
    @Query( value = "select * from wx_user_account where shop_id=?1 and account_type=?2 and kit_balance>=?3",nativeQuery=true)
    List<WxUserAccount> findByShopIdAndAccountType(int shopId, int accountType,int balance);

//	@Modifying
//	@Query(value = "update wx_user_account w set w.total_count=w.total_count+?3,w.balance=w.balance+?3,w.update_time=SYSDATE() where w.account_type=?2 and w.user_id=?1",nativeQuery=true)
//	void updateWxUserAccount(Integer userId,Integer accountType,Integer count);

	List<WxUserAccount> findByUserId(Integer userId);
}
