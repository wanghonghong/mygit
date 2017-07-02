package com.jm.mvc.vo.order;

import com.jm.mvc.vo.PageItem;
import lombok.Data;

import java.util.Map;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/10/27
 */

@Data
public class OrderVo {

    private String hxAccount;

    private Integer userid;

    private String headImg;

    private String nickname;

    private Integer shopId;

    private Integer roleId;

}
