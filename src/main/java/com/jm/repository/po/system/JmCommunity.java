package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>聚社区表</p>
 *
 * @author whh
 * @version latest
 * @date 2016/1/6
 */
@Data
@Entity
public class JmCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty("联系邮箱")
    private Integer staff;

    @ApiModelProperty("qq号")
    private String qq;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    public JmCommunity(){
        this.createDate = new Date();
    }

}
