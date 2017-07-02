package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


/**
 * <p>聚客活动佣金</p>
 *
 * @author chenyy
 * @version latest
 * @date 2017/05/16
 */
@Data
@Entity
public class JkActCommission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("微信用户id")
    private Integer wxUserId;

    @ApiModelProperty("活动id")
    private Integer activityId;

    @ApiModelProperty("佣金金额 单位为分")
    private Integer money;

}
