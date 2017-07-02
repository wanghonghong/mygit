package com.jm.repository.po.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>官方图文</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/2 17:14
 */
@Data
@Entity
@ApiModel(discriminator = "官方图文(项目图文/乐享图文/培训通知)")
public class ImageText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("1:项目图文 2：乐享图文 3:培训通知")
    private Integer typeId;

    @ApiModelProperty("店铺id")
    private Integer shopId;

    @ApiModelProperty("版式选择 /1:仿微信页 2：自由编辑 3:H5")
    private Integer formatCode;

    @ApiModelProperty("样式选择 1:2:3")
    private Integer styleCode;

    @ApiModelProperty("图文分类")
    private Integer imageTextType;

    @ApiModelProperty("图文标题")
    private String imageTextTile;

    @ApiModelProperty("作者")
    private String authorName;

    @ApiModelProperty("分享语")
    private String shareText;

    @ApiModelProperty("主图")
    private String imageUrl;

    @ApiModelProperty("更多好料")
    private String readLinkAdd;

    @ApiModelProperty("底部二维码版式")
    private Integer bottomQrCode;

    @ApiModelProperty("打赏显示版本")
    private Integer rewardFormatCode;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty("图文详情编辑")
    private String imageTextDetail;

    @ApiModelProperty("H5（素材图片库）")
    private String imgResIds;

    @ApiModelProperty("H5 背景颜色")
    private Integer colorId;

    @ApiModelProperty("（素材音频库）")
    private Integer audioResId;

    @ApiModelProperty("创建人员")
    private Integer createStaffId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后一次编辑时间")
    private Date updateTime;

    @ApiModelProperty("是否生效 Y/N")
    private String isValid;

    @ApiModelProperty("点赞人数")
    private Integer reward;

    @ApiModelProperty("预置排序")
    private Integer sort;

    @ApiModelProperty("二维码地址")
    private String qrcodeUrl;

    @ApiModelProperty("1:不可编辑分类")
    private Integer isEdit;

    @ApiModelProperty(value = "上下架")
    private int status;//0上架  1下架

    @ApiModelProperty(value = "视频资源库id 除H5")
    private Integer videoId;

    @ApiModelProperty(value = "视频vid 除H5")
    private String videoUrl;

    @ApiModelProperty(value = "视频封面图资源")
    private Integer videoImg;

    @Lob
    @ApiModelProperty(value = "官方图文详情内容")
    private String detailJson;

    @ApiModelProperty(value = "版本标记 0旧版 1新版")
    private  Integer editionSign;


    @ApiModelProperty(value = "文章类型 0默认类型 1 品牌故事 2 购买须知  3 分销招商 ")
    private  Integer articleType;

    @ApiModelProperty(value = "上架时间")
    private Date upperTime;

    @ApiModelProperty(value = "是否开通奖励 0否 1是")
    @Column(name="isAward",columnDefinition="int default 0" )
    private  int isAward;

    @ApiModelProperty(value = "奖励类型  0积分 1礼券 用逗号拼接")
    private  String awardType;

    @ApiModelProperty(value = "积分类型  0固定 1随机")
    @Column(name="integralType",columnDefinition="int default 0" )
    private  int integralType;

    @ApiModelProperty(value = "奖励秒速 ")
    @Column(name="awardSecond",columnDefinition="int default 0" )
    private  int awardSecond;

    @ApiModelProperty(value = "礼券Id ")
    private  Integer  cardId;




}
