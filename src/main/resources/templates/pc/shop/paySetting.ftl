
	
	<div class="goods-add-norms multimarketing">	        	
				
				<!--微信订单-->
				<div class="goods-sell-set">
				<div class="sell-set-details">
					<div class="sell-set-details-left">
					<div >微信订单</div>
					</div>
					<div class="sell-set-details-right">
					    <input value="${shopSetting.shopSetId!''}" type="hidden" name="shopSetId" id="shopSetId"/>
						<input value="${shopSetting.isOpen!''}" type="hidden" name="isOpen" id="isOpen"/>
						<input value="${shopSetting.brokerageOne!''}" type="hidden" name="brokerageOne" id="brokerageOne"/>
						<input value="${shopSetting.brokerageTwo!''}" type="hidden" name="brokerageTwo" id="brokerageTwo"/>
						<input value="${shopSetting.brokerageThree!''}" type="hidden" name="brokerageThree" id="brokerageThree"/>
							<div class="sell-first-checkbox">
							  <div class=" ">	
								<div class="radioBox" style="margin-right: 10px;">
									 <input type="radio" name="payType" id="radioBox3" value="0" onclick="checkRadio(this.id);" <#if shopSetting.payType??&&shopSetting.payType==0>checked="checked"</#if>/>
									 <label  for="radioBox3"></label>
								</div>
								<span>红包立即发放</span>								
							  </div>
				            </div>
				            <div class="sell-set-details-tips">
								<i class="iconfont icon-lamp"></i>
								<span>订单被点击收货后，系统自动发放红包，每个红包最多200元，单词佣金超过200元，以多个红包发送</span>
							</div>
							
				            <div class="sell-first-checkbox">
							  <div class="">	
								<div class="radioBox" style="margin-right: 10px;">
									 <input type="radio" name="payType" id="radioBox4" value="1" onclick="checkRadio(this.id);" <#if shopSetting.payType??&&shopSetting.payType==1>checked="checked"</#if>/>
									 <label for="radioBox4"></label>
								</div>
								<span>红包延时发放</span>																
							  </div>
				            </div>
				            <div class="sell-set-details-tips">
								<i class="iconfont icon-lamp"></i>
								<span >订单被点击收货后，系统根据延时设置的时间自动发放红包，每个红包最多200元，单词佣金超过200元，以多个红包发送</span>	
							</div>
							
							
								
						    <div class="sell-second-checkbox" style="margin-bottom: 10px;">																					                
					            <div class="sell-third-checkbox" style="margin-left: 12px;">
					            	<span style="margin-right: 20px;">延时设置 </span>
									<div class="radioBox" style="margin-right: 10px;">
										<input type="radio" name="timeSetting" id="radioBox1" value="0" placeholder="请输入延迟天数"  onclick="checkRadio(this.id);" <#if shopSetting.timeSetting??&&shopSetting.timeSetting==0>checked="checked"</#if> />
										<label    for="radioBox1"></label>
									</div>
					                <span>几天</span>
					                <select class="form-control" id="delayDate"  >  
									<option value="-1" selected="selected" >请选择延迟日期</option>  
									<option value="1" <#if shopSetting.delayDate??&&shopSetting.delayDate==1>selected="selected"</#if>   >1</option>
									<option value="50" <#if shopSetting.delayDate??&&shopSetting.delayDate==50>selected="selected"</#if>   >50</option>
									</select> 
				            	</div>
				            	<div class="sell-third-checkbox" style="margin-left: 92px;">					                									
									<div class="radioBox" style="margin-right: 10px;">
										<input type="radio" name="timeSetting" id="radioBox2" value="1" onclick="checkRadio(this.id);" <#if shopSetting.timeSetting??&&shopSetting.timeSetting==1>checked="checked"</#if>/>
										<label  for="radioBox2"></label>
									</div>
					                <span>次月</span>						                
									<input type="text" class="laydate-icon form-control" id="nextMonth" value="<#if shopSetting.nextMonth??>${shopSetting.nextMonth?string('yyyy-MM-dd HH:mm:ss')}</#if>" name="nextMonth" placeholder="开始时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"/>
				            	</div>									
				            </div>
				            
				            <div class="sell-first-checkbox">
							  <div class="">	
								<div class="radioBox" style="margin-right: 10px;">
									 <input type="radio" name="payType" id="radioBox5" value="2" onclick="checkRadio(this.id);" <#if shopSetting.payType??&&shopSetting.payType==2>checked="checked"</#if>/>
									 <label for="radioBox5"></label>
								</div>
								<span>人工审核发放</span>								
							  </div>
				            </div>
				            
				            <div class="sell-set-details-tips">
								<i class="iconfont icon-lamp"></i>
								<span>订单被点击收货后，等待财务人员点击具体单笔佣金后，系统启动红包发送，每包最多200元，单笔佣金超过200元，以多个红包发送</span>
							</div>	
										        	
					</div>
				</div>
				</div>
				<div style="margin: 61px 0 69px;" class="col-xs-12">
					<div class="btn btn-lightorange btn-radius-5 btn-lg col-xs-offset-5" onclick="shopSetting.paySetting()" >保存</div>
				</div>						
	         </div>