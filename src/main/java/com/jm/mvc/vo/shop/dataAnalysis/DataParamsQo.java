package com.jm.mvc.vo.shop.dataAnalysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>数据分析参数</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/10 10:15
 */
@Data
public class DataParamsQo {
    private Integer tabIndex; // 1：日常增减 2：分类资料 3：访问行为
    private Date beginTime;
    private Date endTime;
    private String flag; //今日 7天 本月 历史
    private int platForm; // 1:微信 2：微博 0:ALL

    //商品
    private String productName;// 商品名称
    private Integer groupId; //商品分类
    private Integer status; //1在售  2停售  3下架   4售完下架

    //收支
    private Integer orderStatus;//订单状态
    private Integer revenueTypeId; //收入分类
    private Integer payTypeId; //支出分类
    private String orderNum; //订单编码
    private String transactionId; //收款编号
    private String showTotalMoney; //Y:查询总金额 N：否

    // 活动
    private String activityName;
    private Integer activityId;
    private Integer activityTypeId;
    private Integer activityStatus;

    // 菜单
    private int menuId;

    private String sort; // 排序

    @ApiModelProperty(value = "当前页")
    private Integer curPage;
    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public DataParamsQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
