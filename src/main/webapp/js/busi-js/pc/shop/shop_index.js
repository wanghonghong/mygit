CommonUtils.regNamespace("shop", "index");
shop.index = (function(){

	var _goAuthPage = function(shopid){
		var authUrl=CONTEXT_PATH+"/auth/page";
		$.ajax({
			type : 'GET',
			url: authUrl,
			success : function(data) {
				var url =data+"?shop_id="+shopid;
				//window.open(data+"?shop_id="+shopid);
				window.location.href=url;
			}
		});
	};

	var _init = function(){

		$(document).delegate(".getinto", "click", function() {
			var url=$(this).attr("url");
			var isauth=$(this).attr("data");
			var shopid=$(this).attr("data-id");
			var roleid=$(this).attr("data-roleid");
			if(isauth==0){
				var elem = document.getElementById('dialoginfo2');
				dialog({
					title: "提示",
					content: elem,
					cancelValue: '关闭',
					cancel: function () {
					}
				}).width(500).height(150).showModal();

			}else{
				var geturl=CONTEXT_PATH+"/shop_manager/"+shopid+"/"+roleid;
				var res = $.ajaxJson(geturl,"");
				if(res.data.code==0){
					window.location.href=CONTEXT_PATH+"/shop_manager";
				}else{
					window.location.href=CONTEXT_PATH+"/shop";
				}
			}

		});


		/*$(document).delegate(".lg-btn , .add-shop", "click", function() {
			window.open(CONTEXT_PATH+'/createshop');

		});
*/
		$(".agreement-group .iconfont").click(function(){
			$(this).toggleClass("icon-checkboxactive");
			$(this).toggleClass("icon-checkbox");
		});

		closecreateshop = function(data){
			$.zxClosemsgbox({
				document: $("body"),
				onClose: function() {
				}
			});
		};
		toShopManager = function(url){

			window.location.href=url;
		};

	};

	var _createShop = function(){
		var data = {};
		var newUrl = CONTEXT_PATH + "/shop/new_shop";
		$.ajaxJsonGet(newUrl, null, {
			"done": function (res) {
				data = res.data;
				data.CONTEXT_PATH = CONTEXT_PATH;
				jumi.template('shop/new_shop',data,function(tpl){
					$('#shopIndexbody').empty();
					$('#shopIndexbody').html(tpl);
				});

			}
		});
	};

   var _wbAuth = function (shopId) {
       window.open (STATIC_URL+"/tpl/wb/wbAuth.html?shopId="+shopId);
       // window.open(CONTEXT_PATH + "/shop");
       // jumi.template('wb/wbAuth',function(tpl){
		//    $('body').empty();
       //     $('body').html(tpl);
       // });
   }


    return {
		init :_init,
		createShop:_createShop,
		goAuthPage:_goAuthPage,
        wbAuth:_wbAuth
	};
})();

function popMsg(){
	var elem = document.getElementById('createshop');
	memudialog=dialog({
		id:'show-dialog',
		width: 880,
		height: 550,
		title: '店铺创建',
		content: elem 
	});
	memudialog.show();
};


