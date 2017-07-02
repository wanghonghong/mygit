package com.jm.repository.jpa.wb;

import com.jm.repository.po.wb.WbUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>微博用户</p>
 *
 * @version latest
 * @Author whh
 * @Date 2017/3/6
 */
public interface WbUserGroupRepository extends JpaRepository<WbUserGroup,Integer> {

    WbUserGroup findByGroupidAndWbUid(Long groupid,Long wbUid);

    List<WbUserGroup> findByWbUid(Long wbUid);

}
