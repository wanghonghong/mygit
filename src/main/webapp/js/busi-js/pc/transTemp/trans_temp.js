/**命名空间*/
CommonUtils.regNamespace("trans","temp");

/**
 * 新增运费模板
 * @author cj
 * @date 2016-06-16
 */
trans.temp.addTransTemp=function(transTempName,transTarrays){

    var transTemplatesForCreateVo ={};
	var transTemplates_={};
    var transRelationList_ = new Array();
	transTemplates_.templatesName = transTempName;
	transTemplates_.creatTime = (new Date()).Format("yyyy-MM-dd hh:mm:ss");
	for(var n=0;n<transTarrays.length;n++){
			var transTemplatesRelation ={};
			var sendareaIds_ = transTarrays[n][0].join(',');
			var sendAreaName_ = transTarrays[n][1].join(',');
			var  firstNumber_ = transTarrays[n][2][0];
			var  transFare_ = Number(transTarrays[n][2][1])*100;
			var  nextNumber_ = transTarrays[n][2][2];
			var  nextTransFare_ =Number(transTarrays[n][2][3])*100;
		    transTemplatesRelation.sendAreaId = sendareaIds_;
	    	transTemplatesRelation.sendArea = sendAreaName_;
	    	transTemplatesRelation.firstNumber = firstNumber_;
	    	transTemplatesRelation.transFare = transFare_;
	    	transTemplatesRelation.nextNumber = nextNumber_;
	    	transTemplatesRelation.nextTransFare =nextTransFare_;
			transRelationList_.push(transTemplatesRelation);	
	}

    transTemplatesForCreateVo.transTemplates = transTemplates_;
    transTemplatesForCreateVo.transRelationList = transRelationList_;
    var jsonStr = JSON.stringify(transTemplatesForCreateVo);
    var url =CONTEXT_PATH+"/add_trans_templates";
    trans.temp.ajaxPost(url,jsonStr,shopId);

};
trans.temp.updateTransTemp=function(transTempId_,shopId,transTempName,creatTime,transTarrays){

    var transTemplatesForUpdateVo ={};
	var transTemplates_={};

    var transRelationList_ = new Array();
	transTemplates_.templatesId = transTempId_;
	transTemplates_.templatesName = transTempName;
	transTemplates_.creatTime = creatTime;
	transTemplates_.shopId =shopId;
	transTemplates_.updateTime = (new Date()).Format("yyyy-MM-dd hh:mm:ss");
	for(var n=0;n<transTarrays.length;n++){
			var transTemplatesRelation ={};
			var sendareaIds_ = transTarrays[n][0].join(',');
			var sendAreaName_ = transTarrays[n][1].join(',');
			var  firstNumber_ = transTarrays[n][2][0];
			var  transFare_ = Number(transTarrays[n][2][1])*100;
			var  nextNumber_ = transTarrays[n][2][2];
			var  nextTransFare_ = Number(transTarrays[n][2][3])*100;
			transTemplatesRelation.templatesId = transTempId_;
		    transTemplatesRelation.sendAreaId = sendareaIds_;
	    	transTemplatesRelation.sendArea = sendAreaName_;
	    	transTemplatesRelation.firstNumber = firstNumber_;
	    	transTemplatesRelation.transFare = transFare_;
	    	transTemplatesRelation.nextNumber = nextNumber_;
	    	transTemplatesRelation.nextTransFare =nextTransFare_;
			transRelationList_.push(transTemplatesRelation);
	}

	transTemplatesForUpdateVo.transTemplates = transTemplates_;
	transTemplatesForUpdateVo.transRelationList = transRelationList_;
    var jsonStr = JSON.stringify(transTemplatesForUpdateVo);
    var url =CONTEXT_PATH+"/edit_trans_templates/";
    trans.temp.ajaxPost(url,jsonStr,shopId);

};


trans.temp.ajaxPost=function(url,jsonStr,shopId){
	
	$.ajax({
		type:"POST",
		contentType : 'application/json',
        url:url,  
        processData : false,  
        dataType : 'json',
        data : jsonStr,  
        success : function(data) {
        	
        	if(data.code=="0"){
        		//alert(data.msg);
        	}else{
        		//alert(data.msg);
        	 }
        	 url = CONTEXT_PATH + "/get_trans_templates_list";
        	// window.location.href=url;
			$.ajaxHtmlGet(url, null, {
				done: function (res) {
					if(res.code==0){
						$("#contentBox").empty();
						$("#contentBox").html(res.data);
					}
				}
			});
        },  
        error : function() {  
            alert('Err...');
        }
	});
};


