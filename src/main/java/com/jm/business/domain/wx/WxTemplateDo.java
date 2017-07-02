package com.jm.business.domain.wx;

import lombok.Data;

/**
 * 模板消息
 * @author chenyy
 *
 */
@Data
public class WxTemplateDo {
	
	//------------以下为公用字段  不管哪种类型的都必须传------------
    /**
     * 用户openId
     */
    private String touser;
    /**
     * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
     */
    private String url;
    
    /**
     * appid
     */
    private String appid;
    
    /**
     * 类型  1：发货通知     2：退货通知     3：签收通知     4：退款通知    5：佣金通知   6：下级用户发生通知
     * 7：消息精推  8:商品状态变更通知  9：发送注册通知  10:佣金待办推送  12:回大叔订单预约  13：回大叔积分通知
     */
    private int type;
    
    /**
     * 模板消息第一行显示内容（属于第二标题）
     */
    private String first;
    /**
     * 备注消息
     */
    private String remark;
    
  
    
  //------------以下为部分公用字段，根据类型------------
    /**
     * 订单号（发货通知，退货通知，签收通知  需要传本字段）
     */
    private String orderNum;

    /**
     * 订单号（单位，目前回大叔有用到）
     */
    private String company;
    
  
    /**
     * 物流公司名称（发货通知与签收通知  需要传本字段）
     */
    private String logisticsName;
    
    /**
     * 物流单号 （发货通知与签收通知  需要传本字段）
     */
    private String logisticsNum; 
    
    
    //--------------以下为每个模板独立字段-------------
    
    //-----以下为退货通知需要的字段
    private String refundCount;//退货总量
    private String refundStatus;//退货方式
    
    //-----以下为签收通知需要的字段
    private String orderStatus;//订单状态  如：已签收
    private String singTime;//签收时间
    
    //-----以下为退款通知需要的字段
    private String refundReason;//退款原因
    private String refundAmount;//退款金额
    
    //-----以下为佣金通知需要的字段
    private String incomeType;//收入类型
    private String incomeMoney;//收入金额
    private String incomeTime;//到账时间
    
    //以下为待办任务通知需要的字段
    private String waitForTask;//待办任务
    private String delayTask;//已延期任务

    //注册提示
    private String registerPerson; //注册人
    private String registerNum;//注册号码
    private String registerTime;//注册时间

}
