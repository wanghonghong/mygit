<div class="jm-table  minh">
		<ul class="table-hander">
			<li>头像</li>
			<li style="width:25%;">昵称-姓名-手机号</li>
			<li>订单金额</li>
			<li>佣金比例</li>
			<li>金额</li>
			<li>平台</li>
			<li>状态</li>
			<li>订单详情</li>
		</ul>
		<#list commissionList as item>
			<input type="hidden" id="commission_id" value="${item.commission_id!}"/>
			<ul class="table-container">
				<li>
					<input type="hidden" id="orderInfoId" value="${item.order_info_id!}"/>
					<div class="table">
						<div class="table-cell vertical-middle">
							<a href="#" class="thumbnail"> <img onerror="commission.nofind()"
								src="${item.headimgurl!}" alt="暂无图片" class="avt"> </a>
						</div>
					</div></li>
				<li style="width:25%;"><span>${item.we_chat_name!}</span><br>
				<span>${item.user_name!}</span><br>
				<span>${item.phone_number!}</span></li>
                <li>￥${(item.total_price/100)?string("0.00")}</li>
                <li>${item.brokerage}%</li>
                <li>￥${(item.commission_price/100)?string("0.00")}</li>

				<#if item.plat_form==0>
                    <li>微信</li>
				</#if>
				<#if item.plat_form==1>
                    <li>微博</li>
				</#if>

				<#if item.status==0>
				<li>成功</li>
				</#if>
				<#if item.status==1>
				<li>失败</li>
				</#if>
				<#if item.status==2>
				<li>取消</li>
				</#if>
				<#if item.status==3>
				<li>未发放</li>
				</#if>
				<li>
					<div class="btn  btn-lightgray btn-sm dialog" onclick="commission.sendDig('${item.commission_id}')">发放操作</div>
                    <div id="dialoginfo${item.commission_id}" class="dialoginfo dialoginfo-lstk mallBuilding">
                            <div class="select select-ffcz">
                                <div class="form-group">
                                    <label class="margin-right-xs">未发放金额</label>
                                    <input type="text" class="form-control" value="${(item.commission_price/100)?string("0.00")}" disabled/>
                                </div>
                                <div class="form-group">
                                    <label class="margin-right-m">发放金额</label>
                                    <input type="text" class="form-control" value="${(item.commission_price/100)?string("0.00")}" disabled/>
                                </div>
                                <div  class="form-group">
                                    <label class="margin-right-m">发放方式</label>
                                    <div class="radioBox margin-top-s">
                                        <input type="radio" id="radioBox${item.commission_id}" value=""  checked="checked"  />
                                        <label for="radioBox${item.commission_id}"></label>
                                    </div>
                                    <span class="tips-cont padding-left-s">微信</span>
                                    <#--<div class="radioBox margin-top-s">
                                        <input type="radio" name="sexradio" id="radioBox2" value="" />
                                        <label   for="radioBox2"></label>
                                    </div>
                                    <span class="tips-cont padding-left-s">银行</span>
                                    <div class="radioBox margin-top-s">
                                        <input type="radio" name="sexradio" id="radioBox3" value="" />
                                        <label   for="radioBox3"></label>
                                    </div>
                                    <span class="tips-cont padding-left-s">支付宝</span>-->
                                </div>
                                <div>
                                    <div class="btn btn-lightgray btn-radius-5 btn-lg col-xs-offset-3" onclick="commission.sendCommission('${item.commission_id}')">发送红包</div>
                                    <#--<div class="btn btn-lightorange btn-radius-5 btn-lg ">再次发送红包</div>-->
                                </div>
                        </div>
                    </div>
				</li>
				</ul>
		</#list>
	<#--一共有${counts}条数据-->
    <input type="hidden" id="count" name="count" value="${counts}" />
	</div>