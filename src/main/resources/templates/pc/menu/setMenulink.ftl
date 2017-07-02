<style>
	.dialoginfo-menu .menu-table .table-hander {
	  background: none;
	}
	.dialoginfo-menu .menu-table .table-hander li {
	  padding: 0;
	  color: #000000;
	  border-left: none;
	  border-right: none;
	}
	.dialoginfo-menu .menu-table .table-hander li span {
	  display: inline-block;
	  padding: 5px;
	  margin-bottom: 5px;
	  width: 90%;
	  border-bottom: 1px solid #eee;
	}
	.dialoginfo-menu .menu-table .tag-details-content {
	  width: 100%;
	}
	.dialoginfo-menu .menu-table .tag-details-content .tag-details {
	  width: 20%;
	  float: left;
	  margin-top: 5px;
	}
	.dialoginfo-menu .menu-table .tag-details-content .tag-details li {
	  margin-left: 9px;
	  margin-bottom: 10px;
	  width: 123px;
	}
	
</style>
<div id="setMenuLink" class="dialoginfo dialoginfo-menu dhk">
	<div class="jm-table menu-table">
		<ul class="table-hander">
				<li><span>商城</span></li>
				<li><span>我的</span></li>
				<li><span>活动</span></li>
				<li><span>图文</span></li>
				<li><span>其他</span></li>
		</ul>
	<div class="tag-details-content" id="tag-details-content">		
		
	</div>
	</div>
	<div id="products" class="products">
	    
	</div>
	<div id="page"></div>
</div>
<#--<script src="${basePath}/js/third-js/underscore.js" type="text/javascript" charset="utf-8"></script>-->
<#--<script src="${basePath}/js/our-js/module-js/module.js" type="text/javascript" charset="utf-8"></script>-->
<script>
		var listJson = '${linklist}';
		var shopId = '${jmuser.shopId!}';
		var listJsonData = JSON.parse(listJson);
        var array = _.where(listJsonData,{'parent_id':0});
        var array_1 = _.where(listJsonData,{'parent_id':1});
        var array_2 = _.where(listJsonData,{'parent_id':7});
        var array_3 = _.where(listJsonData,{'parent_id':16});
        var array_4 = _.where(listJsonData,{'parent_id':20});
        var array_5 = _.where(listJsonData,{'parent_id':24});
		
		var length = array.length;
		var len1 = array_1.length;
		var len2 = array_2.length;
		var len3 = array_3.length;
		var len4 = array_4.length;
		var len5 = array_5.length;
		var tpl1 = '',tpl2='',tpl3='',tpl4='',tpl5='';
		var ulTpl = '';
		for(var i=0;i<length;i++){
			ulTpl+='<ul class="tag-details" id="tag_'+i+'"></ul>';
		}
		$('#tag-details-content').append(ulTpl);
		for(var j=0;j<len1;j++){
			
			if(array_1[j].link_type=='1'&&array_1[j].link_name!='二维码海报'){
				tpl1+="<li class='sort' onClick=\"setMemuLink('"+array_1[j].type+"','"+array_1[j].link_name+"','"+array_1[j].link_key+"','"+array_1[j].link_url+"','"+shopId+"')\" >"+					
				array_1[j].link_name+
		         "<input type='checkbox'  />"+
		         "<label class='iconfont icon-avoid' ></label>"+		           
			"</li>";
			}
			if(array_1[j].link_name=='二维码海报' && array_1[j].link_type=='1'){
				tpl1+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_1[j].type+"','"+array_1[j].link_name+"','"+array_1[j].link_key+"','"+array_1[j].link_url+"','"+shopId+"')\" >"+					
				array_1[j].link_name+
				"<span></span>"+			           
				"</li>";
			}
			if(array_1[j].link_name!='二维码海报' && array_1[j].link_type=='2'){
				tpl1+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_1[j].type+"','"+array_1[j].link_name+"','"+array_1[j].link_key+"','"+shopId+"')\">"+					
				array_1[j].link_name+
				"<span></span>"+			           
				"</li>";
			}		
		}
		for(var j=0;j<len2;j++){
			if(array_2[j].link_type=='1'&&array_2[j].link_name!='二维码海报'){
				tpl2+="<li class='sort' onClick=\"setMemuLink('"+array_2[j].type+"','"+array_2[j].link_name+"','"+array_2[j].link_key+"','"+array_2[j].link_url+"','"+shopId+"')\" >"+					
				array_2[j].link_name+
		         "<input type='checkbox'  />"+
		         "<label class='iconfont icon-avoid' ></label>"+		           
			"</li>";
			}
			if(array_2[j].link_name=='二维码海报' && array_2[j].link_type=='1'){
				tpl2+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_2[j].type+"','"+array_2[j].link_name+"','"+array_2[j].link_key+"','"+array_2[j].link_url+"','"+shopId+"')\" >"+					
				array_2[j].link_name+
				"<span></span>"+			           
				"</li>";
			}
			if(array_2[j].link_name!='二维码海报' && array_2[j].link_type=='2'){
				tpl2+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_2[j].type+"','"+array_2[j].link_name+"','"+array_2[j].link_key+"','"+shopId+"')\">"+					
				array_2[j].link_name+
				"<span></span>"+			           
				"</li>";
			}		
		}
		
		for(var j=0;j<len3;j++){
			
			if(array_3[j].link_type=='1'&&array_3[j].link_name!='二维码海报'){
				tpl3+="<li class='sort' onClick=\"setMemuLink('"+array_3[j].type+"','"+array_3[j].link_name+"','"+array_3[j].link_key+"','"+array_3[j].link_url+"','"+shopId+"')\" >"+					
				array_3[j].link_name+
		         "<input type='checkbox'  />"+
		         "<label class='iconfont icon-avoid' ></label>"+		           
			"</li>";
			}
			if(array_3[j].link_name=='二维码海报' && array_3[j].link_type=='1'){
				tpl3+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_3[j].type+"','"+array_3[j].link_name+"','"+array_3[j].link_key+"','"+array_3[j].link_url+"','"+shopId+"')\" >"+					
				array_3[j].link_name+
				"<span></span>"+			           
				"</li>";
			}
			if(array_3[j].link_name!='二维码海报' && array_3[j].link_type=='2'){
				tpl3+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_3[j].type+"','"+array_3[j].link_name+"','"+array_3[j].link_key+"','"+shopId+"')\">"+					
				array_3[j].link_name+
				"<span></span>"+			           
				"</li>";
			}		
		}
		
		for(var j=0;j<len4;j++){
			
			if(array_4[j].link_type=='1'&&array_4[j].link_name!='二维码海报'){
				tpl4+="<li class='sort' onClick=\"setMemuLink('"+array_4[j].type+"','"+array_4[j].link_name+"','"+array_4[j].link_key+"','"+array_4[j].link_url+"','"+shopId+"')\" >"+					
				array_4[j].link_name+
		         "<input type='checkbox'  />"+
		         "<label class='iconfont icon-avoid' ></label>"+		           
			"</li>";
			}
			if(array_4[j].link_name=='二维码海报' && array_4[j].link_type=='1'){
				tpl4+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_4[j].type+"','"+array_4[j].link_name+"','"+array_4[j].link_key+"','"+array_4[j].link_url+"','"+shopId+"')\" >"+					
				array_4[j].link_name+
				"<span></span>"+			           
				"</li>";
			}
			if(array_4[j].link_name!='二维码海报' && array_4[j].link_type=='2'){
				tpl4+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_4[j].type+"','"+array_4[j].link_name+"','"+array_4[j].link_key+"','"+shopId+"')\">"+					
				array_4[j].link_name+
				"<span></span>"+			           
				"</li>";
			}		
		}
		
		for(var j=0;j<len5;j++){
			
			if(array_5[j].link_type=='1'&&array_5[j].link_name!='二维码海报'){
				tpl5+="<li class='sort' onClick=\"setMemuLink('"+array_5[j].type+"','"+array_5[j].link_name+"','"+array_5[j].link_key+"','"+array_5[j].link_url+"','"+shopId+"')\" >"+					
				array_5[j].link_name+
		         "<input type='checkbox'  />"+
		         "<label class='iconfont icon-avoid' ></label>"+		           
			"</li>";
			}
			if(array_5[j].link_name=='二维码海报' && array_5[j].link_type=='1'){
				tpl5+="<li class='addgoods-btn' onClick=\"setMemuLink('"+array_5[j].type+"','"+array_5[j].link_name+"','"+array_5[j].link_key+"','"+array_5[j].link_url+"','"+shopId+"')\" >"+					
				array_5[j].link_name+
				"<span></span>"+			           
				"</li>";
			}
			if(array_5[j].link_name!='二维码海报' && array_5[j].link_type=='2'){
				tpl5+="<li class='addgoods-btn' onclick=\"getUrlValueRequest('"+array_5[j].type+"','"+array_5[j].link_name+"','"+array_5[j].link_key+"','"+shopId+"')\">"+					
				array_5[j].link_name+
				"<span></span>"+			           
				"</li>";
			}		
		}

		$('#tag_0').append(tpl1);
		$('#tag_1').append(tpl2);
		$('#tag_2').append(tpl3);
		$('#tag_3').append(tpl4);
		$('#tag_4').append(tpl5);
		$('#setMenuLink').find('.sort').on('mouseover',function(){
			$('.sort').removeClass('sort-active');
			$(this).addClass('sort-active');
		})
		$('#setMenuLink').find('.sort').on('mouseout',function(){
			$('.sort').removeClass('sort-active');
			
		})
		
</script>
