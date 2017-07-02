package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>公告表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/11/24
 */
@Data
@Entity
public class ZbNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "公告查看人类别:1、总部人员 2、渠道商")
    private Integer lookType;

    @ApiModelProperty(value = "公告状态:1、未发布（存草稿箱） 2、已发布")
    private Integer status;

    @ApiModelProperty(value = "公告类别：总部公告(1、系统公告 2、其他公告)  渠道商公告：(1、系统公告2、功能使用3、市场政策4、市场活动5、费用结算6、公司动态7、行业动态)")
    private Integer type;

    @ApiModelProperty(value = "公告标题")
    private String title;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "公告内容")
    private String noticeContext;

    @ApiModelProperty(value = "发布时间")
    private Date createDate;

    @ApiModelProperty(value = "发布人Id")
    private Integer userId;

    @ApiModelProperty(value = "可查看公告的部门列表")
    private String departments;

    @ApiModelProperty(value = "可查看公告的渠道商列表")
    private String joins;

    public ZbNotice(){
        this.createDate = new Date();
    }
}

