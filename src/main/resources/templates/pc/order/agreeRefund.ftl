<div id="agreeRefund" class="dialoginfo">
    <div class="m-tab2">
        <ul>
            <li class="z-sel">退款操作</li>
            <li>退款原因</li>
        </ul>
    </div>
    <input type="hidden" id="id" name="id" value="${orderRefund.orderRefundId}" />
    <input type="hidden" id="refundMoney" name="refundMoney" value="${((orderRefund.refundMoney!0)/100)?string("0.00")}"/>
    <input type="hidden" id="refundType" name="refundType" value="${orderRefund.refundType}" />
    <div class="m-refund">
        <#--<div class="m-refund-do f-mb-m"><i class="iconfont icon-caution"></i><span>该次退款因技术故障未实际退款</span></div>-->
        <table class="g-table-layout">
            <tbody>
            <tr>
                <td rowspan="2"><span>订单金额（元）</span><br /><span><font color="#f89a14">${((orderRefund.productMoney+orderRefund.sendFee!0)/100)?string("0.00")}</font></span></td>
                <td><span>运费金额（元）<font>${((orderRefund.sendFee!0)/100)?string("0.00")}</font></span><br /><span></span></td>
            </tr>
            <tr>

                <td><span>商品金额（元）<font>${((orderRefund.productMoney!0)/100)?string("0.00") }</font></span><br /><span></span></td>
            </tr>
            </tbody>
        </table>
        <div class="col-xs-9 f-mt-m g-table-refund">
            <div class="u-txt u-txt-rel">
                <div class="u-rb">
                    <input type="radio" name="buy-r" id="radioBox1" value="1" onchange="agreeRefund.changePW();" />
                    <label for="radioBox1"></label>
                </div>
                <label for="radioBox1" class="u-rd-wd">按卖家申请退款</label>
                <input type="text" class="ipt-txt f-tr" name="applyRefund" id="applyRefund" readonly="readonly" value="" placeholder="元" />
            </div>
            <div class="u-txt u-txt-rel">
                <div class="u-rb">
                    <input type="radio" name="buy-r" id="radioBox2" value="2" onchange="agreeRefund.changePW();" />
                    <label for="radioBox2"></label>
                </div>
                <label for="radioBox2" class="u-rd-wd">协议申请退款</label>
                <input type="text" class="ipt-txt f-tr" name="consultRefund" id="consultRefund" onkeyup="clearNoNum(this)" readonly="readonly" value="" placeholder="元" />
            </div>
            <div class="f-cb">
                退款支付方式
                <div class="u-btn-smltgry z-sel"><span>微信公众号红包</span><i class="iconfont icon-avoid"></i></div>
                <div class="u-btn-smltgry"><span>已外部支付退款</span><i class="iconfont icon-avoid"></i></div>
            </div>
        </div>
        <div class="col-xs-9 f-mt-m g-table-refund f-mb-s" style="height: 110px;">
            <div class="u-txt u-txt-rel">
                <div class="u-rb">
                    <input type="radio" name="buy-r" id="radioBox3" value="3" onchange="agreeRefund.changePW();" />
                    <label for="radioBox3"></label>
                </div>
                <label for="radioBox3" class="u-rd-wd">拒绝退款</label>
                <textarea name="shared" id="refuseReason" class="ipt-txt txtarea" placeholder=""></textarea>
            </div>
        </div>
            <#if orderRefund.refundStatus==0>
                <div class="u-btn-box1 f-mt-m">
                    <input type="button" class="u-btn-mddkgry f-mr-xs" onclick="agreeRefund.closeRefund();" value="取消" />
                    <input type="button" class="u-btn-mddkorg" onclick="agreeRefund.saveAgreeRefund(${orderRefund.orderInfoId!'' },${statue})" value="确认提交" />
                </div>
            </#if>
    </div>
    <div class="m-refund">
        <div class="g-table-refund1">
            <label class="u-lb-sm">退款原因</label>
            <div class="u-txt col-xs-9" >
                <textarea name="shared" class="ipt-txt txtarea" disabled="disabled">${orderRefund.refundReason!''}</textarea>
            </div>
            <#--<label class="u-lb-sm">商家图片</label>
            <div class="u-txt col-xs-9">
                <img class="refundimg" src="css/pc/img/no_picture-bg.png"  />
                <img class="refundimg" src="css/pc/img/no_picture-bg.png"  />
                <img class="refundimg" src="css/pc/img/no_picture-bg.png"  />
                <img class="refundimg" src="css/pc/img/no_picture-bg.png"  />
            </div>-->
            <div class="u-btn-box1">
                <input type="button" class="u-btn-mddkorg" value="关闭" />
            </div>
        </div>
    </div>
</div>

<!-- 提交退款操作-->
<script src="${basePath}/js/busi-js/pc/order/agreeRefund.js" type="text/javascript" charset="utf-8"></script>
