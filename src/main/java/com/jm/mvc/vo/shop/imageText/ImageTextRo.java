package com.jm.mvc.vo.shop.imageText;

import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.repository.po.shop.ShopResource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 10:45
 */
@Data
public class ImageTextRo {
    @Data
    public class ImgTextRo{
        private Integer id;

        @ApiModelProperty("1:项目图文 2：乐享图文")
        private Integer typeId;

        @ApiModelProperty("版式选择 /1:仿微信页 2：自由编辑 3：H5")
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

        @ApiModelProperty("图文详情编辑")
        private String imageTextDetail;

        @ApiModelProperty("点赞人数")
        private Integer reward;

        @ApiModelProperty("二维码地址")
        private String qrcodeUrl;

        @ApiModelProperty("1:不可编辑分类")
        private Integer isEdit;

        @ApiModelProperty("创建时间")
        private Date createTime;

        @ApiModelProperty("H5 背景颜色")
        private Integer colorId;

        @ApiModelProperty(value = "上下架")
        private int status;//0上架  1未上架

        @ApiModelProperty(value = "视频vid 除H5")
        private String videoUrl;

        @ApiModelProperty("0:未点赞 1：已经点赞")
        private Integer dzStatus;


        @ApiModelProperty(value = "官方图文详情内容")
        private String detailJson;

        @ApiModelProperty(value = "版本标记 0旧版 1新版")
        private  Integer editionSign;

        @ApiModelProperty(value = "文章类型 0默认类型 1 品牌故事 2 购买须知  3 分销招商 ")
        private  Integer articleType;
        @ApiModelProperty(value = "是否开通奖励 0否 1是")
        private  int isAward;

        @ApiModelProperty(value = "奖励类型  0积分 1礼券 用逗号拼接")
        private  String awardType;

        @ApiModelProperty(value = "积分类型  0固定 1随机")
        private  int integralType;

        @ApiModelProperty(value = "奖励秒速 ")
        private  int awardSecond;

        @ApiModelProperty(value = "礼券Id ")
        private  Integer  cardId;

    }
    @Data
    public class ImgTextTypeRo{
        private Integer id;
        @ApiModelProperty("分类名称")
        private String typeName;
    }

    @ApiModelProperty("官方图文")
    private ImgTextRo imgTextRo;
    @ApiModelProperty("图文分类")
    private List<ImgTextTypeRo> imageTextTypes;
    @ApiModelProperty("H5图文按钮")
    private List<ImageTextRelateRo>  buttonList;
    @ApiModelProperty("H5图文图片素材")
    private List<ShopResourceRo>  imgResList;
    @ApiModelProperty("音频")
    private ShopResourceRo audioRes;
    @ApiModelProperty("视频封面图--图片资源")
    private ShopResourceRo videoRes;
    @ApiModelProperty("视频资源地址")
    private ShopResourceRo videoResoure;

}
