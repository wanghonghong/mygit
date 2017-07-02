		var shopid=0;
		$(document).ready(function() {
			
			$(".u-btn-weixin").click(function(){
				window.open(authPageUrl+'?shop_id='+shopid);
				var elem = document.getElementById('dialoginfo1');
				dialog({
					title: "提示",
					content: elem,
					cancelValue: '重新授权',
					cancel: function () {

					},
					okValue: '授权已完成',
					ok: function() {
						var shopid = $("#shopId").val();
						var okurl=basePath1+"/shop/okshop/"+shopid;
						$.ajaxJson(okurl,"",{
							"done":function (res) {
								console.log(res.data.code);
								if(res.data.code==0){//成功
									var elem1 = document.getElementById('dialoginfo2');
									dialog({
										title: "",
										content: elem1,
									}).width(430).showModal();

									//向微信公众号表内插入数据
									countDown(5,basePath1+"/shop");
								}else{ //授权未成功
									$("#show-qrcode1").hide();
									$("#aginDl1").hide();
									$("#show-qrcode2").show();
									$("#aginDl2").show();
									var elem = document.getElementById('dialoginfo3');
									dialog({
										title: "提示",
										content: elem,
										okValue: '关闭',
										ok: function() {
										}
									}).width(500).height(150).showModal();
								}
							}
						});

					}
				}).width(500).height(150).showModal();
				
			});
			
			//创建店铺
			  $("#addshop").click(function() {
					  if( $("#checkBox1").is(":checked")==false){
							alert("请勾选聚米为谷服务协议！");
						  return;
					  }
				    var shopId = $("#shopId").val();
					var shopName = $("#shopName").val();
					var typeId = $("#typeId").val();
					var specificAddr = $("#specificAddr").val();
					var provinceCode=$("#province").find("option:selected").attr('data-code');
					var cityCode=$("#city").find("option:selected").attr('data-code');
					var districtCode=$("#district").find("option:selected").attr('data-code');
					var province=$("#province").val();
					var city=$("#city").val();
					var district=$("#district").val();
				  if(shopName==""){
					  errMsg("请填写店铺名称！");
					  return;
				  }
				  if(typeId==""){
					  errMsg("请选择主营类目！");
					  return;
				  }
				  if(specificAddr==""){
					  errMsg("请填写详细地址！");
					  return;
				  }
				  if(province==""){
					  errMsg("请选择省份！");
					  return;
				  }
				  if(city==""){
					  errMsg("请选择城市！");
					  return;
				  }
				  if(district==""){
					  errMsg("请选择地区！");
					  return;
				  }
					var dataVo = {};
					dataVo.provinceCode=provinceCode;
					dataVo.cityCode=cityCode;
					dataVo.districtCode=districtCode;
					dataVo.province=province;
					dataVo.city=city;
					dataVo.district = district;
					dataVo.shopName = shopName;
					dataVo.shopType = typeId;
					dataVo.specificAddr = specificAddr;
					dataVo.shopId = shopId;
					var data = JSON.stringify(dataVo);
					var istrue=ajaxPost(basePath1+'/createshop',data);
					if(istrue){
						$("#div1").hide();
						$("#div2").show();
					}

				});

			//选择主营商品
			$("#prtype li").click(function() {
				var text = $(this).text();
				var id = $(this).attr("data-id");
				$("#prname").text(text);
				$("#typeId").val(id);
			});

            //上一步
            $("input[name$='topbtn']").click(function() {
				$("#div1").show();
				$("#div2").hide();
			});

            //完成按钮
			$("input[name$='okbtn']").click(function() {

			});


				});

		
		
		function ajaxPost(url,data){
			$(".loading").show();
			var istrue = false;
			 $.ajax({  
			        type : 'POST',  
			        contentType : 'application/json',  
			        url:url, 
			        processData : false,
			        async:false, 
			        dataType : 'json',  
			        data : data,  
			        success : function(data) {  
			        	if(data.code==0){
			        		shopid=data.msg;
			        		istrue = true;
			        	}else{
							var dm = new dialogMessage({
								type:1,
								title:'操作提醒',
								fixed:true,
								msg:data.cause,
								isAutoDisplay:false
							});
							dm.render();
			        		istrue = false;
			        	}
			        	$(".loading").hide();
			        }
			    });  
			 return istrue;
		 }
		
		function countDown(secs,surl){
			 var jumpTo = document.getElementById('jumpTo');
			 jumpTo.innerHTML=secs;  
			 if(--secs>-1){
			     setTimeout("countDown("+secs+",'"+surl+"')",1000);     
			     }     
			 else{       
			     location.href=surl;     
			     }     
			 }

			 function errMsg(data){
				 var dm = new dialogMessage({
					 type:2,
					 title:'操作提醒',
					 fixed:true,
					 msg:data,
					 isAutoDisplay:false
				 });
				 dm.render();
			 }