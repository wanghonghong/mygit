<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<script type="text/javascript">
				var basePath1='${basePath}';
				var authPageUrl='${authPageUrl}';
		</script>
	</head>
	<body>
		<div class="hander-pc">
		    <div class="hander-top">
			  <div class="content-pc">
			  	<label></label>
			  	<div class="top-memu">
			  		  <img src="${THIRD_URL}/img/pc/shopimg2.png" /> 
			  		<span>店铺管理
			  			<i class="iconfont icon-belowtriangle"></i>
			  		</span><br>
			  		<ul class="user-memu-ul">
			  			<li> <a>店铺管理</a> </li>
			  			<li> <a>软件代理</a> </li>
			  			<li> <a>运营服务</a> </li>
			  			<li> <a>图文手创</a> </li>
			  			<li> <a>媒体流量</a> </li>
			  			<li> <a>个人资料</a> </li>
			  			<li> <a href="${basePath}/loginout">退出</a> </li>
			  		</ul>
			  	</div>
		    </div>
			</div>
			<div class="hander-middle">
				  	<div class="logo-region">
				  		<img src="${THIRD_URL}/img/pc/logo1.png" class="logo-img"/>
				  	</div>
				  	<div class="memu-region">
				  		 <ul class="memu-li">
				  			<li class="active"><a href="#">店铺管理</a></li>
						    		<li><a href="#">软件代理平台</a></li>
						    		<li><a href="#">运营服务平台</a></li>
						    		<li><a href="#">图文手创平台</a></li>
						    		<li><a href="#">O2O媒体流量平台</a></li>
				  		 </ul>
				  	</div>
			</div>
		</div>
		<div class="content-pc padding-top-hander">
			<div class="step-bar-box">
				<div class="current-step">
					<label><span>1</span><i class="iconfont icon-avoid"></i></label>
					<h4>新增店铺资料</h4>
				</div>
				<div >
					<label><span>2</span><i class="iconfont icon-avoid"></i></label>
					<h4>微信公众号授权</h4>
				</div>
				<div >
					<label><span>3</span><i class="iconfont icon-avoid"></i></label>
					<h4>微信企业支付授权</h4>
				</div>
			</div>	
			
			
			<div class="issue-all">
			<!--新增店铺资料-->
			<input type="hidden" id="shopId" name="shopId" value="${shop.shopId!''}" />
			<div class="goods-classify-choose" id="type-goods-classify">			
			<div  class="pd10 row">
				<div class="xzdp-box">
					<div class="form-group col-xs-12 margin-bottom-m">
						<label>店铺名称</label>
						<input type="text" id="shopName" name="shopName" value="${shop.shopName!''}"  class="form-control"/>
					</div>
					<div class="form-group col-xs-12 margin-bottom-m">
						<label>主营商品</label>
						<select name="test" id="select2" selectValue="${shop.shopType!''}">
							<#list prls as pr>
								<option value="${pr.typeId!''}">
								${pr.typeName!''}
								</option>
							</#list>
						</select>
					</div>
					<div class="form-group col-xs-12 margin-bottom-m box-width" id="distpicker" >
						<label>&nbsp所在地</label>
						 <select class="form-control"  id="province"></select>
						<div style="line-height: 38px;">&nbsp省&nbsp </div>
						 <select class="form-control"  id="city"></select>
						<div style="line-height: 38px;">&nbsp市&nbsp </div>
						<select class="form-control"  id="district"></select>
						<div style="line-height: 38px;">&nbsp县&nbsp </div>
					</div>
					<div class="form-group col-xs-12 margin-bottom-m">
	            		<label>详细地址</label>
						<textarea class="form-control" placeholder="" id="specificAddr" name="specificAddr" >${shop.specificAddr!''}</textarea>
					</div>
					<div  class="agreement-group col-xs-12">
					 	 <i class="iconfont icon-checkboxactive"></i>
						 <span>我已阅读并同意</span>《<a href="#">聚米为谷商家版代理销售服务和结算协议和担保交易服务协议</a>》
					</div>
				</div>				
			</div>
					
			<div class="xzdp-btn">
				<div class="goods-classify-btn btn btn-darkorange btn-radius-5 btn-lg col-xs-offset-5" id="addshop" >下一步</div>
			</div>
			</div>
			<!--新增店铺资料-->
			<!--微信公众号授权-->
			<div class="goods-existing-classify issue-steps-hide" id="type-goods-existing">
			
			<div class="authority text-center">
				<div class="font-color-orange ">绑定微信公众号，把店铺和微信打通</div>
				<div class="font-color-black">绑定后即可在这里管理您的公众号，聚米为谷提供比微信官方后台更强大的功能</div>
				<div class="btn btn-darkgreen btn-radius-5 btn-lg" id="show-qrcode1">
					<i class="iconfont icon-wechat"></i><span>我有微信公众号，立即授权</span><i class="iconfont icon-ensure"></i>
				</div>
			</div>
			<div class="xzdp-btn">
				<div style="padding:0 30px;" class="btn  goods-existing-btn-last btn-lightgray btn-radius-5 btn-lg col-xs-offset-4">上一步</div>
				<div style="margin-left:5px" id="addbtn-next" class="btn goods-existing-btn-next btn-darkorange btn-radius-5 btn-lg">下一步</div>
			</div>
			</div>
			<!--微信公众号授权-->
			<!--微信企业支付授权-->
			<div class="goods-add-norms issue-steps-hide" id="type-add-norms">
			<div class="pd10 row">
				<div class="xzdp-box authority-finish">
					<div class="font-color-black col-xs-12 margin-bottom-m">设置自有微信支付，买家使用微信支付付款购买商品时，货款将直接进入您微信支付对应的财付通账户。</div>
					<div class="form-group col-xs-12">
						<label><span style="color:red">*</span>&nbsp;商户号</label>
						<input type="text" class="form-control" id="mchId" name="mchId"/>
					</div>
					<div class="form-group col-xs-12 margin-bottom-m img-tips">
						<span><i class="iconfont icon-lamp margin-right-m"></i>请填写微信发给您的邮件中的商户号，不是您财付通的商户号</span>
					</div>
					<div class="form-group col-xs-12">
						<label><span style="color:red">*</span>&nbsp;密钥</label>
						<input type="text" class="form-control" id="appKey" name="appKey" />
					</div>
					
				</div>
			</div>
			<div class="xzdp-btn">
				<div style="padding:0 30px;" class="btn  goods-existing-btn-last btn-lightgray btn-radius-5 btn-lg col-xs-offset-4">上一步</div>
				<div style="margin-left:5px" class="btn goods-existing-btn-next btn-darkorange btn-radius-5 btn-lg dialog" id="okbtn">完成</div>
			</div>
			</div>
			<!--微信企业支付授权-->
			</div>								
		</div>
		<div id="dialoginfo1" class="dialoginfo dhk" style="display: none;">
			<div class="select dialog-js login-tc">
				<div class="dialog-jsimg"></div>
				<div class="dialog-jstxt1 text-center"><span class="login-tis">在新窗口中完成微信公众号授权</span></div>
			</div>			
		</div>
		<div id="dialoginfo" class="dialoginfo dhk">
			<div class="select dialog-js login-tc">
				<div class="dialog-jsimg"></div>
				<div class="dialog-jstxt1 text-center"><span class="login-tis">恭喜你，授权完成！</span></div>
				<div class="dialog-jstxt1 text-center" ><span><font color="#e87017" id="jumpTo">5</font>秒 后将自动进入您的新店铺管理系统</span></div>
			</div>			
		</div>
		
		<div class="footer-pc" style="clear:both;bottom: 0;height: 33px;left: 0;line-height: 33px;position: fixed;width: 100%;z-index: 20;">
					 <label>
					 	Copyright  版权由聚米为谷科技所有
					 </label>
		</div>

		<script>
		$(function(){
			
			$("#addbtn-next").click(function(){
				$("#type-add-norms").removeClass("issue-steps-hide").siblings().addClass("issue-steps-hide");
				$(".step-bar-box div:eq(2)").addClass("current-step").siblings().removeClass("current-step");
			});
		});
		
		
		
		//********************初始化地区控件
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
		//********************初始化地区控件
		</script>
	</body>
</html>
