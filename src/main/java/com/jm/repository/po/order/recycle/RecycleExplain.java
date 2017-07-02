package com.jm.repository.po.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>回收说明</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */

@Data
@Entity
@ApiModel("回收说明")
public class RecycleExplain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("图片地址")
    private String imgUrl;

    @ApiModelProperty("启用模块1")
    private String module1;

    @ApiModelProperty("启用模块2")
    private String module2;

    @ApiModelProperty("启用的模块1")
    private int usingModule1;

    @ApiModelProperty("启用的模块2")
    private int usingModule2;

    @ApiModelProperty("启用的模块")
    private int usingModule;

    @ApiModelProperty("描述0")
    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    private String node;

    @ApiModelProperty("描述1")
    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    private String node1;

    private Integer shopId;
}
