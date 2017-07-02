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
 * <p>商家微博</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/28 9:41
 */
@Data
@Entity
@ApiModel(description = "商家微博信息")
public class WbShopUser {
    @Id
    private Long uid;
    private String accessToken;
    private Date createTime;
    private Long createAt; //	access_token的创建时间，从1970年到创建时间的秒数。
    private Long expireIn; // 	access_token的剩余时间，单位是秒数。
    private Long appkey; // access_token所属的应用appkey。
    @ApiModelProperty(value = "是否已经拉取过用户信息：0未拉取过，1已拉取过")
    private int isGet;
}
