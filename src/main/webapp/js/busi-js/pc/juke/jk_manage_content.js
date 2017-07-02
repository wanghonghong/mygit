
CommonUtils.regNamespace("juke", "manage");

juke.manage = (function(){
	
	var _init = function(){
		_createList();
		_bind();
	};

	//绑定事件
	var _bind = function(){
		//tab切换页
		$('#jk_m_tab li').click(function () {
			var index = $(this).data('index');
			$(this).addClass('z-sel').siblings().removeClass('z-sel');
			_queryList(index);
		});
	};

	var _createList = function () {
		jumi.template('jk/jk_manage_create',function (tpl) {
			$('#activityContent').empty();
			$('#activityContent').html(tpl);
		});
	};


	var _queryList =  function (index) {
		if(index == 0){
			_createList();
		}
		else if(index == 1){
			jumi.template('jk/jk_manage_list',function (tpl) {
				$('#activityContent').empty();
				$('#activityContent').html(tpl);
			});
		}

	};



	return{
		init:_init
	};
})();