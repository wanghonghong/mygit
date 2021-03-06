package com.jm.mvc.vo.zb.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>加盟Ro</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/30
 */
@Data
@ApiModel(description = "加盟Vo")
public class ZbJoinVo {

    private Integer userId;

    private String userName;

    private String phoneNumber;

    private Integer applyRole;

    @ApiModelProperty("申请状态：0:未申请审核 1、申请未审核 2、申请被驳回 3、已通过审核")
    private Integer status;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("加盟子类型：1、个人加盟 2、公司加盟")
    private Integer subType;

    private String companyName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ZbJoinVo(){
        this.curPage = 0;
        this.pageSize = 20;
    }

}
