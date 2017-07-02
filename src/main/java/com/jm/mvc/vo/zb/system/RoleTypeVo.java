package com.jm.mvc.vo.zb.system;

import lombok.Data;

import java.util.Date;

/**
 * <p>角色类型Vo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class RoleTypeVo {

    private Integer id;

    private String typeName;

    private String userName;//创建人名字

    private Date createDate;


}
