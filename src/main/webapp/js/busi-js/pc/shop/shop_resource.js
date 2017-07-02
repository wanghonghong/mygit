var Dialog = function(options){
	this.options = options || {};
}
	   		Dialog.prototype = {
	   		initialize:function(){
	   			
	   			
	   		},
	   		init:function(){
	   			this.initialize();
	   			this._onbindEvent();
	   		},
	   		_onbindEvent:function(){
	   			
	   			
	   			
	   			var self = this;
	   			var shopId = this.options.shopid;
	   			var imgId = this.options.imgid;
	   			var flagCode = this.options.flagcode;
	   			var myfileId = this.options.myfileid;
	   			self._showDialog();
	   			$(document).delegate(".dialog-title .icon-delete1", "click", function() {
		        	  self.DialogBox.close();
		      	 });
	   			$('.tab').find('li').on('click',function(){
	   				var dom = $(this);
	   				var index = dom.index();	   				
	   				dom.addClass('active').siblings('li').removeClass('active');
	   				$('#content').find('#dialog_'+index).show().siblings().hide();
	   			})
	   			$('.popupFile').on('change',function(){
	   				fileuplod_proGroupItemImg(imgId,myfileId,flagCode);
	   			})
	   			
	   			$(document).on('click','.btn-lightgray',function(){
	   				var dom = $(this);
	   				var parent = dom.parent();
	   				parent.remove();
	   				var id = parent.find('input[name="mapid"]').val();
	   				
	   				//ajax请求删除数据
	   				$.ajax({
	   					url:CONTEXT_PATH+'/deleteShopResource/',
	   					dataType:'json',
	   					data:{
	   						id:id
	   					},
	   					type:'POST',
	   					success: function(data){
	   						if(data.code===0){
	   							
	   							
	   							if(self.DialogBox){
	   								
	   								var id = self.DialogBox.id;
	   								var tpl = self._renderMapList();
	   								
	   								$('#'+id).find('#mapList').empty().append(tpl);
	   							}
	   							
	   						}else{
	   							dialog({
	   								content:data.msg
	   							})
	   							var tpl = self._renderMapList();							
   								$('#'+id).find('#mapList').empty().append(tpl);
	   						}
	    					
	  					}    				
	   				})
	   				
	   			})
	   		},
	   		_renderMapList:function(){
	   			var self = this;
	   			var shopId = self.options.shopid;
	   			$('#dialog_1').empty();
	   			var tpl = '<ul class="clearfix" id="mapList" style="overflow:hidden;height:300px;overflow-y:auto;">';
	   			
	   			
	   			$.ajax({
   					url:CONTEXT_PATH+'/shopResource/'+shopId,
   					dataType:'json',
   					async:false,
   					data:{
   						resType:1
   					},
   					type:'GET',
   					success: function(json){
   						
    					if(json.code===0){
    						var data = JSON.parse(json.data);		
    		   				var len = data.length;
    		   				for(var i=0;i<data.length;i++){
    		   					tpl += '<li class="floatleft margin-right-l">'+
    									'<div class="existing-classify-info lstk-img">'+
    									'<div class="goods-classify-img">'+
    							        	'<img width="100" height="100" src="'+data[i].res_url+'" alt="" />'+		
    							       	'</div></div>'+
    									'<div class="lstk-tittle">'+
    										'<div class="checkBox" style="border:none;">'+
    											'<input type="hidden" name="resType" value="'+data[i].res_type+'"/>'+
    											'<input type="hidden" name="mapid" value="'+data[i].id+'"/>'+
    											'<input type="radio" name="map"  value="'+data[i].res_url+'" />'+
    																
    										'</div>'+	
    										'<span> 选中</span></div>'+
    									'<div  class="btn  btn-lightgray btn-sm lstk-btn"> 删除 </div>'+	
    								'</li>'
    		   				}
    		   				tpl+='</ul>';
    					}else{
    						tpl='';
    					}
    					$('#dialog_1').append(tpl);
    					
    					
  					}    				
   				})
   				return tpl;
	   			
	   		},
	   		_randNum: function(n) {
	   			var rnd = "";
	   			for (var i = 0; i < n; i++) {
	   				rnd += Math.floor(Math.random() * 10);
	   			}
	   			return rnd;
	   		},
	   		
	   		_showDialog:function(){
	   			var self = this;
	   			var tpl = '';
	   			var id = "dialog_one";
	   			tpl = this._renderMapList();
	   			var elem = $('#dialoginfo');
	   			
	   			dialog({
					title:'历史图库',
					id:id,
					content: elem,					
					okValue: '保存',								
					ok: function() {
						var imgPath = $("input[name='map']:checked").val();
						var imgId = self.options.imgid;
						var flagCode = self.options.flagcode;
						if(1==flagCode){
							update_proGroupItemImg(imgId,imgPath);
						}else if(2==flagCode){
							addHistory_proImg(imgId,imgPath);
						}
						
					},
				}).width(600).height(400).showModal();
	   		},
	   		
	   		
	   		
	   		
	   		_offbindEvent:function(){
	   			$('.btn-lightgray').off('click');
	   			$('.btn-lightorange').off('click');
	   		},
	   		_dispose:function(){
	   			if(typeof this.DialogBox!=='undefined'){
	   				this.DialogBox = null;
	   			}
	   			this._offbindEvent();
	   		}
	   	}
	   function openShopResource(imgId,flagCode,shopId,myfileId){
	   			
	   			var d = new Dialog({
	   				imgid:imgId,
	   				flagcode:flagCode,
	   				shopid:shopId,
	   				myfileid:myfileId
	   			});
	   		   	d.init();
	   		   	
	   	}
/////////////////////////////////////////////////////////////
	   	