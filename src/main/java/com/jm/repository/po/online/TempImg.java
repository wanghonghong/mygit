package com.jm.repository.po.online;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class TempImg {

    @ApiModelProperty(value="主键id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tempId;

    @ApiModelProperty(value="文件id")
    private String fileId;

    @ApiModelProperty(value ="状态 0 正常 1 删除")
    private int status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建时间")
    private int type;
}
