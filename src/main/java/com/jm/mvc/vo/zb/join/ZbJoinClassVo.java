package com.jm.mvc.vo.zb.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>加盟Ro</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/30
 */
@Data
@ApiModel(description = "加盟Vo")
public class ZbJoinClassVo {

    private Integer userId;

    private String userName;

    private String phoneNumber;

    private Integer applyRole;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("加盟子类型：1、个人加盟 2、公司加盟")
    private Integer subType;

    private String companyName;

    @ApiModelProperty(value = "关注开始时间")
    private String startTime;

    @ApiModelProperty(value = "关注结束时间")
    private String endTime;

    @ApiModelProperty(value = "地区")
    private String area;

    @ApiModelProperty(value = "昵称")
    private String nickNmae;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ZbJoinClassVo(){
        this.curPage = 0 ;
        this.pageSize = 20;
    }
}
