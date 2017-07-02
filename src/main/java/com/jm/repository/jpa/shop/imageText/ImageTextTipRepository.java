package com.jm.repository.jpa.shop.imageText;

import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.shop.imageText.ImageTextTip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>图文点赞</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/20 10:20
 */
public interface ImageTextTipRepository extends JpaRepository<ImageTextTip,Long> {
    @Query(value = "select b.headimgurl from image_text_tip a left join wx_user b on a.user_id = b.user_id " +
            " where a.image_text_id=?1 and a.status = 1 and a.pay_id is not null order by a.create_time desc limit 20",nativeQuery = true)
    List getHeadImgUrlByImageTextId(Integer imageTextId);

    @Query("select count(a) from ImageTextTip a where a.payId is not null and a.status =1 and a.imageTextId=?1")
    Integer getCountImageTextTipByImageTextId(Integer imageTextId);
}
