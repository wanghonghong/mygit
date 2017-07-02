<script>
	function addTransTemplate(){
		//window.location.href="${basePath}/to_add_transTemp";
		var url = "${basePath}/to_add_transTemp";
        $.ajaxHtmlGet(url, null, {
            done: function (res) {
                $("#contentBox").empty();
                $("#contentBox").append(res.data);
            }
        });
	}
    function editTransTemplate(id){
		    var url ="${basePath}/toEditTransTemp/"+id;
            //window.location.href=url;
        $.ajaxHtmlGet(url, null, {
            done: function (res) {
                $("#contentBox").empty();
                $("#contentBox").append(res.data);
            }
        });
	}
	/*function delTransTemplate (id){

        var d = dialog({
            title: '提示',
            content: '是否确认删除运费模板?',
            width: 300,
            okValue: '确定',
            ok: function () {
                var url =CONTEXT_PATH+"/deleteTransTemp?tempId="+id;
                $.ajaxJsonDel(url,"",{
                    "done":function (res) {
						if(res.data.msg==0){
                            $("#temp"+id).remove();
						}else{
                            alert(res.data.msg);
                        }
                    },
                    "fail":function (res) {
                       // alert(res.data.msg);
                    }

                });
            },
            cancelValue: '取消',
            cancel: function () {

            }
        });
        d.show();
	}*/
    function delTransTemplate (id){


        var args = {};
        args.fn1 = function(){
            delTTitems(id);
        };
        args.fn2 = function(){
        };
        jumi.dialogSure("是否确认删除运费模板?", args);
    }

    function delTTitems(id){
        var url =CONTEXT_PATH+"/deleteTransTemp?tempId="+id;
        $.ajaxJsonDel(url,"",{
            "done":function (res) {
                if(res.data.code==0){
                    $("#temp"+id).remove();
                    var dm = new dialogMessage({
                        type: 1,
                        title: '操作提醒',
                        fixed: true,
                        msg: res.data.msg,
                        isAutoDisplay: false

                    });
                    dm.render();
                }else{

                    var dm = new dialogMessage({
                        type: 2,
                        title: '操作提醒',
                        fixed: true,
                        msg: res.data.msg,
                        isAutoDisplay: false

                    });
                    dm.render();
                }
            },
            "fail":function (res) {
                // alert(res.data.msg);
            }

        });
    }

</script>

		<div class="content-pc " id="transTempList">

			<div class="main-content-pc" style="padding: 23px 24px 23px 5px;">
			    <div class="mallBuilding">
			    	<div class="stock-info-details-right">
			    		<div class="stock-info-details stock-info-yfmb">
			    			<div  class="btn btn-lightgray btn-lg" >
								<i class="iconfont icon-add"></i>
								<span onclick="addTransTemplate()">新建运费模板</span>
							</div>
			    		</div>

			    	</div>


<#if TransTempListVo ??>
			        <#list TransTempListVo as transTempListVo>
						<div id="temp${transTempListVo.transTemplates.templatesId}">
							<div class="goods-standard-stock goods-standard-yfmb" >

								<div class="stock-info-details">
									<div class="stock-info-details-left">
										<div >${transTempListVo.transTemplates.templatesName}</div>
										<i class="iconfont icon-belowtriangle "></i>
									</div>
									<div class="page-left" style="float: right; margin-right: 10px;">
										<#--<div  class="btn  btn-lightgray btn-sm ml"> 复制模板</div>-->
										<div  class="btn  btn-lightgray btn-sm" onclick="editTransTemplate(${transTempListVo.transTemplates.templatesId})"> 修改 </div>
										<div  class="btn  btn-lightgray btn-sm" onclick="delTransTemplate(${transTempListVo.transTemplates.templatesId})"> 删除 </div>
									</div>

								</div>
							</div>
						<div class="jm-table" style="min-height: 100px;font-size: 12px">
									<ul class="table-hander">
										<li style="width: 35%; text-align: left;  padding-left: 10px;s">可配送至</li>
										<li>首件（个）</li>
										<li>运费（元）</li>
										<li>续件（个）</li>
										<li>续件（元）</li>
									</ul>
									 <#list transTempListVo.transRelationList as transRelation>
											<ul class="table-container">

												<li style="width: 35%;text-align: left;  padding-left: 10px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;cursor: pointer;"  class='defaultransnode' >${transRelation.sendArea!''}</li>
												<li>${transRelation.firstNumber!0}</li>
												<li>${(transRelation.transFare/100)?string("0.##")}</li>
												<li>${transRelation.nextNumber!0}</li>
												<li>${(transRelation.nextTransFare/100)?string("0.##")}</li>
											</ul>
									</#list>

						</div>
					</div>

					</#list>


				</#if>



		</div>
			</div>


		</div>
<script>
    $(document).on("click",".defaultransnode",function(){
        var val =$(this).text();

        var dg = dialog({
            title: '地区',
            content: val,
            id:'dialog_title',
            cancelValue: '关闭',
            cancel: function () {
                dialog.get('dialog_title').close().remove();
            },
            width:600
        });
        dg.show();

    })
</script>