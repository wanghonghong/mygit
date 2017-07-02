package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>审核建议表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/10/11
 */
@Data
@Entity
public class JmJoinCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("被审核人userId")
    private Integer userId;

    @ApiModelProperty("审批人")
    private Integer checkerId;

    @ApiModelProperty("审批人")
    private String checkerName;

    @ApiModelProperty("审核内容")
    private String checkContext;

    @ApiModelProperty("审核时间")
    private Date checkTime;

    public JmJoinCheck(){
        this.checkTime = new Date();
    }
}

