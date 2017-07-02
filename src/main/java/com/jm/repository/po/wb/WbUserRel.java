package com.jm.repository.po.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微博用户关联表</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/3 9:33
 */
@Data
@Entity
@ApiModel(description = "微博用户关联表")
public class WbUserRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "微博商城uid   pid 和 uid 父子关系")
    private Long pid;

    @ApiModelProperty(value = "微博用户id")
    private Long uid ;

    @ApiModelProperty(value = "上一级")
    private Long upperOne;

    @ApiModelProperty(value = "上二级")
    private Long upperTwo;

    @ApiModelProperty(value = "是否关注  0:否  1是  ")
    private Integer isSubscribe;

    @ApiModelProperty(value = "关注时间")
    private Date subscribeTime;

    @ApiModelProperty(value = "取消关注时间")
    private Date unSubscribeTime;

    @ApiModelProperty(value = "微博用户所在分组id")
    private Integer groupid ;

    @ApiModelProperty(value = "用户等级")
    private Integer levelId;

    @ApiModelProperty(value = "是否有购买过  0:无  1有")
    private int isBuy;

    @ApiModelProperty(value = "是否分享客  0:否  1是")
    private int isShare;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "店铺用户编号")
    private Integer shopUserId;

}
