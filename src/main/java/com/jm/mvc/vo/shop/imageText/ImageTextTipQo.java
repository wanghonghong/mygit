package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>图文点赞</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/20 11:12
 */
@Data
public class ImageTextTipQo {
    private Integer tabIndex;
    private Integer imageTextId;
    private String imageTextTitle;
    private Date beginTime;
    private Date endTime;
    @ApiModelProperty(value = "平台：0微信，1微博")
    private int platForm;
    private int status; // -1:删除 0：上架 1：下架
    private String telPhone;
    private String userName;
    private String nickName;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;
    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ImageTextTipQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
