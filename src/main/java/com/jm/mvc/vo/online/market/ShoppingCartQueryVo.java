package com.jm.mvc.vo.online.market;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>pc端购物车查询条件</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/9
 */

@Data
public class ShoppingCartQueryVo {

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "开始日期")
    private Date beginDate;

    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "type：0是购物车，1是兴趣单")
    private int type;

    public ShoppingCartQueryVo(){
        this.curPage = 0;
        this.pageSize = 10;
    }

}
