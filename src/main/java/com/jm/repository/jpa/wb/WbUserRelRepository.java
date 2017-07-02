package com.jm.repository.jpa.wb;

import com.jm.repository.po.wb.WbUser;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.repository.po.wx.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>微博用户关联</p>
 *
 * @version latest
 * @Author whh
 * @Date 2017/3/10
 */
public interface WbUserRelRepository extends JpaRepository<WbUserRel,Long> {

    WbUserRel findWbUserRelByPidAndUid(Long pid,Long uid);

    @Query(value = "select * from wb_user_rel where upper_one=?1 ORDER BY id DESC", nativeQuery = true)
    List<WbUserRel> findWbUserRelUpperOneListById(Long id);

    @Query(value = "select * from wb_user_rel where upper_two=?1 ORDER BY id DESC", nativeQuery = true)
    List<WbUserRel> findWbUserRelUpperTwoListById(Long id);

    List<WbUserRel> findByPid(Long pid);

    List<WbUserRel> findByGroupid(int groupid);
}
