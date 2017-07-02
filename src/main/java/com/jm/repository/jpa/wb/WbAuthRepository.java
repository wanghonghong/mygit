package com.jm.repository.jpa.wb;

import com.jm.repository.po.wb.WbAuth;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>微博第三方应用</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/4 10:58
 */
public interface WbAuthRepository extends JpaRepository<WbAuth,String> {

}
