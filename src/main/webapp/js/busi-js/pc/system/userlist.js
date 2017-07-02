CommonUtils.regNamespace("user");

var userId=0;

 function queryuser(){
	var phonenumber = $("#phoneNumber").val();
	var mydata = '{"phoneNumber":"' + phonenumber + '"}';
	$.ajaxJson(basePath+"/queryuserlist",mydata,{
		done:function(res){
			if(res.data.msg!=null){
        		//alert(res.data.msg);
				var dm = new dialogMessage({
					type:1,
					title:'操作提醒',
					fixed:true,
					msg:res.data.msg,
					isAutoDisplay:false
				});
				dm.render();
        		return;
        	}
        	var data=res.data.data;
        	var div ='<div class="jm-font-ming-md"></div>'+
			'<div class="jm-table" >'+
				'<ul class="table-hander">'+
					'<li>头像</li>'+
					'<li>姓名</li>'+
					'<li>手机号</li>'+
					'<li>身份</li>'+
					'<li style="width:15%;">管理</li>'+
				'</ul>';
        	
        	if(data.userId!=null){
				var sqgg='<div  class="btn  btn-lightgray btn-sm" id="dialog2" onclick="dialoga('+data.userId+')" > 点击授权</div>';
				var headimg=basePath+"/css/pc/img/no_picture.png";
						if(data.headImg!=''&&data.headImg!=null){
							headimg=data.headImg;
						}
					var username = "";
				if(data.userName!=null){
					username=data.userName;
				}



        		div +='<ul class="table-container">'+
				'<li>'+
					'<div class="table">'+
						'<div class="table-cell vertical-middle" style="width:10%;">'+
							'<div class="checkBox">'+
							'<input type="checkbox" name="" id="checkbox2" value="" />'+
							 '<label class="iconfont icon-avoid"  for="checkbox2"></label>'+
							'</div>'+
						'</div>'+
						'<div class="table-cell vertical-middle">'+
						  '<a href="#" class="thumbnail">'+
							      '<img src="'+headimg+'" alt="暂无图片" class="avt" style="height: 100px;width: 100px;">'+
						  '</a>'+
						'</div>'+
					'</div>'+
				'</li>'+
				'<li>'+username+'</li>'+
				'<li>'+data.phoneNumber+'</li>'+
				'<li>聚米注册用户</li>'+	
				'<li style="width:15%;">'
				+sqgg+
				'</li>'+
			'</ul>';
				
			}
        	$("#userDiv").empty();
        	$("#userDiv").append(div+'</div>');
			
		}
		
	});
			
}


user.render = function(){
	$("#queryuser").on("click",user.get);
	$(document).delegate("#delrole", "click", function() {
		var id= $(this).attr("data");
		$.ajax({  
	        type : 'POST',  
	        contentType : 'application/json',  
	        url:basePath+'/delroleuser/'+id, 
	        processData : false,  
	        dataType : 'json',  
	        async: false,
	        success : function(data) { 
	        	if(data.code==0){
	        		//alert("删除成功");
					var dm = new dialogMessage({
						type:1,
						title:'操作提醒',
						fixed:true,
						msg:'删除成功',
						isAutoDisplay:false
					});
					dm.render();

	        		shop.commonClick(basePath+'/userlist');
	        	}
	        },  
	        error : function() {  
	           // alert('Err...');
				var dm = new dialogMessage({
					type:1,
					title:'操作提醒',
					fixed:true,
					msg:'Err。。。',
					isAutoDisplay:false
				});
				dm.render();
	        }  
       });
		
	});
	
}

	
	function dialoga(id,roleid){
		userId=id;
		$("input[type=radio][name=roleId][value="+roleid+"]").attr("checked",'checked')
		var elem = document.getElementById('dialoginfo');
		dialog({
			title: "角色选择",
			content: elem,
			okValue: '确定',
			ok: function() {
				save();
			}
		}).width(600).height(300).showModal();
		
	}
	
	function save(){
		var roleid = $("input[name='roleId']:checked").val();
		var mydata = '{"roleId":"' + roleid + '","userId":"' + userId + '"}';  
		 $.ajax({  
			        type : 'POST',  
			        contentType : 'application/json',  
			        url:basePath+'/updateuserrole', 
			        processData : false,  
			        dataType : 'json',  
			        async: false,
			        data : mydata,  
			        success : function(data) {
							shop.commonClick(basePath+'/userlist');
							var dm = new dialogMessage({
								type:1,
								title:'操作提醒',
								fixed:true,
								msg:'授权成功',
								isAutoDisplay:false
							});
							dm.render();
			        }
		       });
		 
	}
	
	
	$(function(){
		user.render();
	});
	

	
