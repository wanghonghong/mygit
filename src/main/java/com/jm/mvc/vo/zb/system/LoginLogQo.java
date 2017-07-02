package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>登录日志vo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class LoginLogQo {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public LoginLogQo() {
        this.curPage = 0;
        this.pageSize = 20;
    }

}
