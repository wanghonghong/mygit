package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 10:30
 */
@Data
public class ImageTextUo {
    @Data
    public class ImgTextUo{
        private Integer id;

        @ApiModelProperty("1:项目图文 2：乐享图文")
        private Integer typeId;

        @ApiModelProperty("版式选择 /1:仿微信页 2：自由编辑  3:H5")
        private Integer formatCode;

        @ApiModelProperty("版本选择H5的情况下--样式选择 1:2:3")
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

        @ApiModelProperty("H5（素材图片库）")
        private String imgResIds;

        @ApiModelProperty("H5 背景颜色")
        private Integer colorId;

        @ApiModelProperty("（素材音频库）")
        private Integer audioResId;

        @ApiModelProperty("音频名称")
        private String audioTitle;

        @ApiModelProperty("点赞人数")
        private Integer reward;

        /*@ApiModelProperty(value = "上下架")
        private Integer status;//0上架  1未上架*/

        @ApiModelProperty(value = "视频资源id 除H5")
        private Integer videoId;

        @ApiModelProperty(value = "视频vid 除H5")
        private String videoUrl;

        @ApiModelProperty(value = "视频封面图")
        private Integer videoImg;

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

    private ImgTextUo imgTextUo;
    private List<ImageTextRelateUo> imageTextRelateList ;

}
