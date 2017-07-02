package com.jm.repository.jpa.shop.activity;
import com.jm.repository.po.shop.activity.ActivitySub;
import com.jm.repository.po.shop.activity.ShopCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>卡卷</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/30
 */
public interface ShopCardRepository extends JpaRepository<ShopCard, Integer>{

}
