package com.jm.mvc.vo.order;

import lombok.Data;

@Data
public class OrderDiscountVo {

    private int cardId;

    private int count;

    private int type;//0 积分 1 卡券

    private Integer orderInfoId;

}
