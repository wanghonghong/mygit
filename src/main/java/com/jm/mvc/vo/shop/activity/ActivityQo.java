package com.jm.mvc.vo.shop.activity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
public class ActivityQo {

        @ApiModelProperty("活动名称")
        private String activityName;

        @ApiModelProperty("红包平台 / 1:微信商城 2：微博商城")
        private String platform;

        @ApiModelProperty("发送目标 / 1:购买商品 2：卡卷赠送 3：收货奖励 4：首次关注的新粉 5：已关注的粉丝")
        private String sendTarger;

        @ApiModelProperty(value = "店铺ID")
        private Integer shopId;
        
        @ApiModelProperty(value = "appid")
        private String appid;
        
        @ApiModelProperty(value = "平台用户id，非微信用户id")
        private Integer userId;

        @ApiModelProperty(value = "状态  0：未开始 1 ：进行中 2：暂停 3：结束 9 删除")
        private String status;

        @ApiModelProperty("活动类型 1:现金红包 2：卡券红包 3：红包墙")
        private Integer type;

        @ApiModelProperty(value = "当前页")
        private Integer curPage;

        @ApiModelProperty(value = "每页显示条数")
        private Integer pageSize;
        
        @ApiModelProperty(value = "创建时间")
        private Date createTime;

        @ApiModelProperty(value = "摇一摇次数 1：摇一次即中 3：摇三次随机中 （此字段只有聚客红包有使用到）")
        private int shakeCount;

        public ActivityQo(){
                this.curPage = 0;
                this.pageSize = 20;
        }

}
