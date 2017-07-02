<div class="jm-table">

    <table class="jm-table-control">
        <thead>
        <tr >
            <th style="width:25%;">商品</th>
            <th style="width:10%;">单价/数量</th>
            <th style="width:10%;">售后</th>
            <th style="width:10%;">下单时间</th>
            <th style="width:10%;">订单状态</th>
            <th style="width:10%;">实付金额</th>
        </tr>
        </thead>
    </table>

    <div id="tableBody1"  class="table-body padding-top-s" >
        <table  class="jm-table-control"  id="jmtable" >
            <tbody>
            <tr class="bg-gray jm-table1">
                <td colspan="6">

                    <ul class="table-xqd" >
                        <li class="floatleft padding-left-s">订单编号：${commissionInfo.orderNum!} &nbsp;支付号流水号：${commissionInfo.payOrderNum!} </li>
                        <li class="service floatright">${commissionInfo.weChatName!}</li>
                    </ul>
                </td>
            </tr>
            <#if detailList??>
                <#list detailList as detail>
                <tr>
                    <td style="width: 25%;">
                        <div class="table-cell vertical-middle padding-s">
                            <a href="#" class="thumbnail" style="width: 100px">
                                <img src="${detail.picSquare!'${THIRD_URL}/img/pc/logo.png'}" alt="暂无图片">
                            </a>
                        </div>
                        <div class="table-cell vertical-middle  padding-left-m">
                            <span >${detail.name!}</span><br>
                            <#--specValueOne=null, specValueTwo=null, specValueThree=null-->
                            <span>${detail.specValueOne!}
                                <#if detail.specValueTwo??>
                                -${detail.specValueTwo!}
                                </#if>
                                <#if detail.specValueThree??>
                                    -${detail.specValueThree!}
                                </#if>
                                </span>
                        </div>
                    </td>
                    <td style="width:10%;"><span>￥${(detail.price/100)?string("0.00")}</span><br><span>（${detail.count!}件）</span></td>
                    <#--1:已付款 ，待发货;2:已发货，待收货;3:订单管理（已收货）;4:申请退款;5:退货退款中;6:已退款，发货管理（已收货）7:订单关闭-->
                    <td style="width:10%;" class="font-color-orange">暂无</td>
                    <td style="width:10%;padding:0 10px;"><span>${commissionInfo.createDate!}</span></td>

                    <td style="width:10%;">
                        <#if commissionInfo.status==1>
                            <span>已付款</span><br/>
                        <#elseif  commissionInfo.status==2>
                            <span>待收货</span><br/>
                        <#elseif  commissionInfo.status==3>
                            <span>已收货</span><br/><span>${commissionInfo.takeDate!}</span>
                        <#elseif  commissionInfo.status==4>
                            <span>申请退款</span><br/>
                        <#elseif  commissionInfo.status==5>
                            <span>退货退款中</span><br/>
                        <#elseif commissionInfo.status==6>
                            <span>已退款</span><br/>
                        <#elseif  commissionInfo.status==7>
                            <span>订单关闭</span><br/>
                        </#if>
                    </td>

                    <td style="width:10%;"><span>￥${(commissionInfo.totalPrice/100)?string("0.00")}</span><br/><span >（含运费：${(commissionInfo.sendFee/100)?string("0.00")}）</span>	</td>
                </tr>
                </#list>
            </#if>
            <tr>
                <td colspan="3"  >
                    <p class="text-left padding-left-s">	<b> 买家留言： </b>${commissionInfo.remark!}</p>
                </td>
                <td colspan="3"  >
                    <p class="text-left padding-left-s">	<b> 客服备注： </b>${commissionInfo.sellerNote!}</p>
                </td>

            </tr>
            </tbody>
        </table>
    </div>