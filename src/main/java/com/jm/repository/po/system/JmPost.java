package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>论坛帖子表</p>
 *
 * @author whh
 * @version latest
 * @date 2016/1/12
 */
@Data
@Entity
public class JmPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型：1，已发布，2，草稿")
    private Integer status;

    @ApiModelProperty(value = "主题分类:1、微博 2、微信")
    private Integer type;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "公告内容")
    private String postContext;

    @ApiModelProperty(value = "是否匿名，1，是，2，否")
    private Integer isHide;

    @ApiModelProperty(value = "帖子特征:1、强烈推荐 2、热帖 3、精")
    private Integer feature;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    public JmPost(){
        this.createDate = new Date();
    }

}
