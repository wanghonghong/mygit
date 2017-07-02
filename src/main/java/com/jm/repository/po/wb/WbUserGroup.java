package com.jm.repository.po.wb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * <p>微博用户分组表</p>
 *
 * @author whh
 * @version latest
 * @date 2017/3/17
 */
@Data
@Entity
public class WbUserGroup {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(value = "分组id")
    private Long groupid;

    @ApiModelProperty(value = "wbUid -- 微博店铺uid")
    private Long wbUid;

    @ApiModelProperty(value = "组名")
    private String name;

    @ApiModelProperty(value = "当前组人数")
    private int count;

    @ApiModelProperty(value = "分组类型，0为关键词分组，2为自定义分组；")
    private int ruleType;

}
