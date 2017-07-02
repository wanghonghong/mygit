package com.jm.repository.po.shop.brokerage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>佣金收费配置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "佣金收费配置")
public class BrokerageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "收费类型：1分销升级收费，2代理收费")
    private int feeType;

    @ApiModelProperty(value = "封面图片")
    private String imgUrl;

    @ApiModelProperty(value = "分享语")
    private String share;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "升级说明")
    private String instruction;

}
