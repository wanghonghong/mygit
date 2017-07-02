package com.jm.mvc.vo.zb.system;

import lombok.Data;

import java.util.Date;

/**
 * <p>岗位Vo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class PostVo {

    private Integer postId;

    private String postName;

    private String userName;//创建人名字

    private Date createDate;


}
