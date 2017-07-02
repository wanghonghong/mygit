
			<div class="mallBuilding multimarketing">
			   <div class="row clearfix">
					<div class="col-xs-5">
						<div class="phone-view phone-view-mini phone-billd phone-marketing">
							<div class="phone-shell-top">
							</div>
							<div class="phone-window">
									<div  class="fxsz">
									</div>
									<div id="app-memu-panel">
										<ul class="app-bottom-memu" id="app-bottom-memu">
										</ul>
									</div>
							</div>
							<div class="phone-shell-bottom">
							</div>
						</div>
					</div>
					<div class="col-xs-7 existing-classify-choose">
						<div class="app-config-region">
			        		<div class="panel  config-panel">
								<div class="panel-heading yysm clearfix">
									<h4 class="floatleft">1 至 3 级免费分销系统设置,本系统免费使用</h4>
									<span class="floatright">运用说明</span>
								</div>
									<div class="close">
							<input type="checkbox" name="switch1" id="switch1" value="${shopSetting.isOpen!}" on-text="开通"  off-text="关闭"  <#if shopSetting.isOpen??&&shopSetting.isOpen==0>checked="checked"</#if> />
						 </div>
								<div class="panel-body" id="switchpanel" >							<!-- style="display: none;" -->
									<div class="official-hande-box">
										<div class="official-content-info">
										<input value="${shopSetting.shopSetId!''}" type="hidden" name="shopSetId" id="shopSetId"/>
										<input value="${shopSetting.payType!''}" type="hidden" name="payType" id="payType"/>
										<input value="${shopSetting.timeSetting!''}" type="hidden" name="timeSetting" id="timeSetting"/>
										<input value="${shopSetting.delayDate!''}" type="hidden" name="delayDate" id="delayDate"/>
										<input value="<#if shopSetting.nextMonth??>${shopSetting.nextMonth?string('yyyy-MM-dd HH:mm:ss')}</#if>" type="hidden" name="nextMonth" id="nextMonth"/>
											<ul>
												<li>
													<span><i>1</i> 级佣金比例</span>
													<div class="form-group"><input type="text" class="form-control" name="brokerageOne" id="brokerageOne" placeholder="请输入百分比" value="${shopSetting.brokerageOne!''}" maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"/></div>
													<span>(只填写本级，即为1级分销系统）</span>
												</li>
												<li>
													<span><i>2</i> 级佣金比例</span>
													<div class="form-group"><input type="text" class="form-control" name="brokerageTwo" id="brokerageTwo" placeholder="请输入百分比" value="${shopSetting.brokerageTwo!''}"  maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"/></div>
													<span>(只填写本级，即为2级分销系统）</span>
												</li>
												<li>
													<span><i>3</i> 级佣金比例</span>
													<div class="form-group"><input type="text" class="form-control" name="brokerageThree" id="brokerageThree" placeholder="请输入百分比" value="${shopSetting.brokerageThree!''}"  maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"/></div>
													<span>(只填写本级，即为3级分销系统）</span>
												</li>
											</ul>
										</div>
										<div class="stock-info-details-right">
											<div class="stock-info-tips">
											    <i class="iconfont icon-lamp"></i>
											    <span>分佣比例填写为 0，代表本系统只预建三级上下级客户关系，暂不执行分销佣金分配</span>
											</div>
										</div>
									</div>

							</div>
						</div>
					</div>
				</div>
				<div style="margin: 61px 0 69px;" class="col-xs-12">
					<div class="btn btn-lightorange btn-radius-5 btn-lg col-xs-offset-5" onclick="shopSetting.shopSetting()">保存</div>
				</div>
			</div>
			</div>

				<!-- 多级分销 -->
		<script src="${basePath}/js/busi-js/pc/shop/shopSetting.js" type="text/javascript" charset="utf-8"></script>
