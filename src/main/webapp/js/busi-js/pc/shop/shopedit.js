/**
 * Created by wxz on 16/8/19
 * 店铺设置
 */
CommonUtils.regNamespace("shop", "shopmanage");
shop.shopmanage=(function(){

	var ajaxUrl = {
		url1:CONTEXT_PATH+'/shop/shopedit',//店铺编辑信息
		url2:CONTEXT_PATH+'/shop/prls',//主营类目
		url3:CONTEXT_PATH+'/shop/shopupdate', //店铺更新
		url4:CONTEXT_PATH+'/shop/shop_qrcode' //二维码
	};
	var ajaxUrlAuth={
		url1:CONTEXT_PATH+'/shop/shop_auth',//认证保险

	};

	var _init = function(){
		//选项卡切换
		_shop_tabchange();
		_shop_initSelect('#shopType');
		_shop_maintype();
	};
	var _shop_tabchange=function(){

		$(".tabli").click(function () {
			var index = $(this).index();
			$(".tabli").removeClass("active");
			$(this).addClass("active");

            $("div[id^='shop_content']").hide().eq(index).show();
            if (index === 0) {
				$.ajaxJsonGet(ajaxUrl.url1,{
					"done":function (res) {
						if(res.code===0){
							jumi.template('shop/shop_baseset',res.data, function (tpl) {

								$('#shop_content_baseset').show();
								_shop_initSelect('#shopType');
								_shop_maintype();
							})
						}
					},
					"fail":function (res) {
					}
				});
			}
			if (index === 1) {
				$.ajaxJsonGet(ajaxUrlAuth.url1, {
					"done": function (res) {
						if(res.code===0) {
							jumi.template('shop/shop_auth',res.data,function (tpl) {
								$('#shop_content_auth').html(tpl);
							})
						}
					},
					"fail":function (res) {

					}
				});

			}
			if (index === 2) {
				//shop/shop_entity
				jumi.template('shop/shop_entitynew', function (tpl) {
					$('#shop_content_entity').html(tpl);
				})
			}
			if (index === 3) {
				_shop_qrode_list();
			}
		});
	};

	//二维码列表
	var _shop_qrode_list = function () {
		$.ajaxJsonGet(ajaxUrl.url4,{
			"done":function (res) {
				if(res.code===0){
					var items =res.data;
					jumi.template('shop/shop_code',items, function (tpl) {
						$('#shop_content_qrcode').html(tpl);
					});
					_shop_qrcode_download();
				}
			},
			"fail":function (res) {
			}
		});
	}

	var _shop_qrcode_download = function () {
		$(".m-shop-ewm ul").find('input').bind('click',function () {
			var url = $(this).parent().parent().find('img').attr('src');
			window.location.href=url;
		})
	}
	
	var _shop_maintype= function () {

		$.ajaxJsonGet(ajaxUrl.url2,{
			"done":function (res) {
				if(res.code===0){
				    var items =res.data;
                    var lng = items.length;
                    var hidval=  $("#shiptypehid").text();
					var tpl = '<option value="-1">--请选择--</option>';
					_.map(items,function(k,v){
						if(k.typeId===hidval){
							tpl+='<option value="'+k.typeId+'" selected="selected">'+k.typeName+'</option>';
						}else{
							tpl+='<option value="'+k.typeId+'">'+k.typeName+'</option>';
						}
					});
					$('#shopType').html(tpl);
					$('#shopType').val(hidval);
				}
			},
			"fail":function (res) {
			}
		});
	};
///时间控件
	var _bindcreatedate = function () {

		$("#shopctdate").datetimepicker({
			showSecond : true,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd',
			stepHour : 1,
			stepMinute : 1,
			stepSecond : 1
		});

	};
	///////门店照片start
	var _shop_baseset_photo=function(){
		$('#imgupload').click(function(){
			var d = new Dialog({
				context_path:CONTEXT_PATH, //请求路径,  必填
				resType:1 ,//图片1，视频2，语音3   必填
				callback:function(url){
					$('#imgUrl').attr('src',url);
				}
			});
			d.render();
		});
	};
///////门店照片end
	var _shop_baseset_area=function () {
		///////所属地区start
		var chooseShopAreaobj = new chooseShopArea();
		chooseShopAreaobj.init();
		///////所属地区end
	}
///////所属地区start
	var chooseShopArea = function(){
	};
	chooseShopArea.prototype = {
		init:function(){
			this._renderArea();
		},
		_renderArea:function(){
			var addrval =$('#basesetaddr').text();
			var $distpicker = $('#distpickerbset');
			if(addrval!==''&&addrval!==null){
				var addrarr = addrval.split(',');
				$distpicker.distpicker({
					province:addrarr[0],
					city: addrarr[1],
					district:addrarr[2],
					autoSelect: false
				});
			}else{
				$distpicker.distpicker({
					province:'',
					city: '',
					district:'',
					autoSelect: false
				});
			}
		},
		_onBindClick:function(){


		},
		_offBindClick:function(){

		}
	};

///////所属地区end
var _shop_baseset_update=function () {

	$("#shop_baseset_save").click(function() {
		var args = {};
		args.fn1 = function(){
			_shop_baseset_save();
		};
		args.fn2 = function(){

		};
		jumi.dialogSure("是否保存该基本设置信息？", args);
	});

}
	var _shop_baseset_save=function () {

			var dataVo = {};
			dataVo.imgUrl=$("#imgUrl").attr("src");
			dataVo.tempId=$("#tempId").val();
			dataVo.shopId=$("#shopId").val();
			dataVo.shopName=$("#shopName1").val();
			dataVo.shopType=$("#shopType").val();
			dataVo.shareLan1=$("#shareLan1").val();
			dataVo.shareLan2=$("#shareLan2").val();
			dataVo.linkMan=$("#linkMan").val();
			dataVo.phoneNumber=$("#phoneNumber").val();
			dataVo.wxNum=$("#wxNum").val();
			dataVo.qqMail=$("#qqMail").val();
			dataVo.provinceCode=$("#provincebset").find("option:selected").attr('data-code');
			dataVo.cityCode=$("#citybset").find("option:selected").attr('data-code');
			dataVo.districtCode=$("#districtbset").find("option:selected").attr('data-code');
			dataVo.province=$("#provincebset").val();
			dataVo.city=$("#citybset").val();
			dataVo.district=$("#districtbset").val();
			dataVo.specificAddr=$("#specificAddr").val();
			dataVo.status=$("#status").val();
			dataVo.createDate=$("#shopctdate").val();
			var data = JSON.stringify(dataVo);
			$.ajaxJson(ajaxUrl.url3,data,{
				"done":function (res) {
					if(res.code===0) {

                        $("#shiptypehid").text($("#shopType").val());
						var dm = new dialogMessage({
							type: 1,
							title: '操作提醒',
							fixed: true,
							msg: '恭喜您，操作成功',
							isAutoDisplay: false

						});
						dm.render();
					}else{
						var dm = new dialogMessage({
							type:2,
							title:'操作提醒',
							fixed:true,
							msg:'对不起，操作失败',
							isAutoDisplay:false

						});
						dm.render();
					}
				},
				"fail":function (res) {
				}
			});

	}

//认证保险
	var _shop_auth_edit=function () {

		$("#shop_auth_save").click(function () {
			var args = {};
			args.fn1 = function(){
				_shop_auth_save();
			};
			args.fn2 = function(){

			};
			jumi.dialogSure("是否保存该认证保险信息？", args);
		});

	}
	var _shop_auth_save=function () {

			var shopAuthUo = {};
			var str="";
			shopAuthUo.directSell=0;
			shopAuthUo.promise=0;
			shopAuthUo.exchange=0;
			shopAuthUo.safety=0;
			shopAuthUo.companyAuth=0;
			shopAuthUo.userAuth=0;
			shopAuthUo.jmAuth=0;

			$("input[name='checkbox']:checked").each(function(){
				var id=$(this).attr("id");
				switch (id){
					case "directSell":
						shopAuthUo.directSell=1;
						break;
					case "promise":
						shopAuthUo.promise=1;
						break;
					case "exchange":
						shopAuthUo.exchange=1;
						break;
					case "safety":
						shopAuthUo.safety=1;
						break;
				}
			});
			$.ajaxJsonPut(ajaxUrlAuth.url1,shopAuthUo,{
				"done":function (res) {
					if(res.code===0){

                        var dm = new dialogMessage({
                            type:1,
                            title:'操作提醒',
                            fixed:true,
                            msg:'恭喜您，操作成功',
                            isAutoDisplay:false
                        });
                        dm.render();

                    }else{
                        var dm = new dialogMessage({
                            type:2,
                            title:'操作提醒',
                            fixed:true,
                            msg:'对不起，操作失败',
                            isAutoDisplay:false
                        });
                        dm.render();

					}
				},
				"fail":function (res) {
				}
			});
	}

	//初始化选择框
	var _shop_initSelect = function (id) {
		$.fn.select2.defaults.set('language', 'zh-CN');
		$(id).select2({
			theme: "jumi",
			language: "en"
		});
	};

	return {
		init :_init,
		shop_maintype:_shop_maintype,
		shop_baseset_photo:_shop_baseset_photo,
		shop_baseset_area:_shop_baseset_area,
		bindcreatedate:_bindcreatedate,
		shop_baseset_update:_shop_baseset_update,
		shop_auth_edit:_shop_auth_edit
	};

})();


