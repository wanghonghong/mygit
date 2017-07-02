package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

import java.util.Date;

/**
 * <p>微信用户表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/15
 */
@Data
@Entity
public class WxUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "关注时间")
    private String appid;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户的标识，对当前公众号唯一")
    private String openid;

    @ApiModelProperty(value = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段")
    private String unionId;

    @ApiModelProperty(value = "首次关注时间")
    private Date fristSubscribeTime;

    @ApiModelProperty(value = "关注时间")
    private Date subscribeTime;

    @ApiModelProperty(value = "跑路时间")
    private Date unSubscribeTime;

    @ApiModelProperty(value = "1男 2女 0未知")
    private Integer sex;

    @ApiModelProperty(value = "地区编号")
    private Integer areaCode;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "上一级")
    private Integer upperOne;

    @ApiModelProperty(value = "上二级")
    private Integer upperTwo;

    @ApiModelProperty(value = "备注")
    private String  remark;

    @ApiModelProperty(value = "分组id")
    private int groupid;

    @ApiModelProperty(value = "是否关注  0:否  1是 98 ")
    private Integer isSubscribe;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "用户等级")
    private Integer levelId;

    @ApiModelProperty(value = "我的小店权限0：没有权限，1：免费版权限，2：收费版权限,3：代理商权限,4：分销商权限")
    private int isMyShop;

    @ApiModelProperty(value = "是否有购买过  0:无  1有 99 ")
    private int isBuy;

    @ApiModelProperty(value = "店铺用户编号")
    private Integer shopUserId;

    @ApiModelProperty(value="最后操作时间")
    private Date lastControlTime;

    @ApiModelProperty(value = "最后购买时间")
    private Date lastBuyTime;

    @ApiModelProperty(value = "是否分享客  0:否  1是")
    private int isShare;

    @ApiModelProperty(value = "用户信息更新时间")
    private Date updateTime;;

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof WxUser&&o!=null ){
            WxUser user = (WxUser) o;
            if(user.getUserId().equals(this.userId)){
                return true;
            }
        }
        return false;

    }
}
