package com.jm.mvc.vo.shop.integral;

/**
 * <p>积分奖励清单</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/10/08
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "店铺积分")
public class IntegralRecordQo {

    @ApiModelProperty(value = "积分类型：1 登录 2 推荐关注 3 购买返利")
    private Integer integralType;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "创建时间开始")
    private Date beginTime;

    @ApiModelProperty(value = "创建时间结束")
    private Date endTime;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public IntegralRecordQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
