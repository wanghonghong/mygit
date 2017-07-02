			   <script>
			   		var basePath = '${basePath}';
			   </script>

			   <div class="row clearfix">
			   		<div class="tab" id="shop_tab">
			      		<ul>
				  			<li class="active tabli" onclick="shopSet();">基本设置</li>
				  			<li class="tabli" onclick="">认证保险</li>
				  			<li class="tabli" onclick="trueShop();">实体门店</li>
			      		</ul>
			      	</div>
			      	<div class="clearfix" id="shop_inner_content">
				<#--	<div class="floatleft col-xs-4">
						<div class="phone-view phone-view-mini phone-billd phone-marketing">
							<div class="phone-shell-top">
							</div>
							<div class="phone-window">
									<div  class="fxsz shop">
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
					<div class="floatleft col-xs-8 djfe-bjth shop-jbsz dialoginfo-lstk">
						<div class="goods-sell-set  margin-top-m"  >
							<div class="sell-set-details">
								<div class="sell-set-details-left">
								<div >店铺设置</div>
										<input type="hidden" name="tempId" id="tempId" value="${shop.tempId!'' }"/>
										<input type="hidden" name="status" id="status" value="${shop.status!'' }"/>
										<input type="hidden" name="shopId" id="shopId" value="${shop.shopId!'' }"/>
								</div>
								<div class="sell-set-details-right">
									<div class="form-group">
					            		<label class="margin-right-m">店铺名称</label>
										<input type="text" class="form-control" placeholder="" name="shopName" id="shopName1" value="${shop.shopName!'' }"/>
									</div>
									<div class="form-group">
					            		<label class="margin-right-m">主营类目</label>
										<select class="form-control" id="shopType" name="shopType">
											<#list prls as pr>
												<option value="${pr.typeId!''}"
												<#if shop.shopType==pr.typeId>selected="selected" </#if>  >
												${pr.typeName!''}
												</option>
											</#list>
										</select>
									</div>
									<div class="form-group">
					            		<label class="margin-right-m">创建时间</label>
										<label class="margin-right-m">&nbsp;&nbsp;&nbsp;${shop.createDate!''}</label>
									</div>


									<div class="form-group stock-info-details-right">
					            		<label class="margin-right-x">店铺LOGO</label>
					            		<div class="stock-info-details stock-info-tips margin-bottom-m">
											<div class="">
												<div class="stock-info-details-add-img img-pzsc">
													<div>
														<div>
															<img alt="" width="100px" height="100px" src="${shop.imgUrl!'' }" id="imgUrl">
															 <i class="iconfont icon-add"></i>
															<input type="button" id="imgupload"/> &ndash;&gt;
														</div>
													</div>

												</div>
											</div>
											<div class="base-info-img-tips">
											<i style="" class="iconfont icon-lamp"></i>
											建议尺寸：长<span>96*96</span>像素
											</div>

										</div>
									</div>
									<div class="form-group">
					            		<label class="margin-right-m">分享语摘要</label>
					            		<textarea class="form-control" placeholder="" id="shareLan1" name="shareLan1">${shop.shareLan1!'' }</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="goods-sell-set">
							<div class="sell-set-details">
								<div class="sell-set-details-left">
								<div >联系我们</div>
								</div>
								<div class="sell-set-details-right">
									<div class="form-group">
					            		<label class="margin-right-m">联系人</label>
										<input type="text" class="form-control" placeholder="" id="linkMan" name="linkMan" value="${shop.linkMan!'' }" />
									</div>
									<div class="form-group">
					            		<label class="margin-right-m">手机号</label>
										<input type="text" class="form-control" placeholder="" id="phoneNumber" name="phoneNumber" value="${shop.phoneNumber!'' }" />
									</div>
									<div class="form-group">
					            		<label class="margin-right-m">微信号</label>
										<input type="text" class="form-control" placeholder="" id="wxNum" name="wxNum" value="${shop.wxNum!'' }" />
									</div>
									<div class="form-group">
					            		<label class="margin-right-m">QQ邮箱</label>
										<input type="text" class="form-control" placeholder="" id="qqMail" name="qqMail" value="${shop.qqMail!'' }" />
									</div>
								        <div class="form-group szd clearfix" id="distpicker">
								          <label class="margin-right-m">所在地</label>
								          <select class="floatleft form-control" id="province"></select>
								          <select class="floatleft form-control" id="city"></select>
								          <select class="floatleft form-control" id="district"></select>
								        </div>
								     <div class="form-group">
					            		<label class="margin-right-m"></label>
										<input type="text" class="form-control" placeholder="" id="specificAddr" name="specificAddr" value="${shop.specificAddr!'' }" />
									</div>

									<div class="form-group">
					            		<label class="margin-right-m">地图标注</label>
					            		<textarea class="form-control" placeholder="" id="shareLan2" name="shareLan2">${shop.shareLan2!'' }</textarea>
									</div>

								</div>
							</div>
						</div>
					</div>-->
					
					</div>

			  </div>
				<div style="margin: 61px 0 69px;">
					<div style="padding:0 30px;" class="btn btn-darkorange btn-radius-5 btn-lg col-xs-offset-6">保存</div>
				</div>
		<script src="${basePath}/js/third-js/distpicker/distpicker.data.js"></script>
 		<script src="${basePath}/js/third-js/distpicker/distpicker.js"></script>
		<script src="${basePath}/js/our-js/map_material.js"></script>
		<script src="${basePath}/js/busi-js/pc/shop/shopedit.js"></script>
        <script src="${basePath}/js/busi-js/pc/shop/shopentity.js"></script>
		<script>
			var chooseArea = function(){
			  };
			  chooseArea.prototype = {
			     init:function(){
			        this._renderArea();
			     },
			     _renderArea:function(){
			        var $distpicker = $('#distpicker');
			        $distpicker.distpicker({
			            province: '${shop.province!'' }',
			            city: '${shop.city!'' }',
			            district: '${shop.district!'' }',
			            autoSelect: false
			        });
			     },
			     _onBindClick:function(){
			         

			     },
			     _offBindClick:function(){

			     }
			  };
			  var choosearea = new chooseArea();
			  choosearea.init();




		</script>
