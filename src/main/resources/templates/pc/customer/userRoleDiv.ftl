
	<div class="m-sell-set1 noboxsw" style="padding-bottom: 10px;">
		<div class="sell-set-top f-mt-s" style="border-bottom: none;">
			<h3 style="padding-left: 8px;">用户信息</h3>
		</div>
		<div class="sell-set-cont m-upgradeuserbox">
			<div class="m-bbsuserinfo-lb">
				<label class="u-lb c-deepgray">昵称： <font class="c-gray1">${nickName!''}</font></label>
				<label class="u-lb c-deepgray">姓名： <font class="c-gray1">${userName!'-'}</font></label>
				<label class="u-lb c-deepgray">手机： <font class="c-gray1">${phoneNumber!'-'}</font></label>
				<label class="u-lb c-deepgray">角色： <font class="c-orange2" id="roleName">${roleName!'-'}</font></label>
			</div>
		</div>
	</div>
	<div class="m-sell-set1 noboxsw f-mt-s" style="padding-bottom: 10px;">
		<div class="sell-set-top f-mt-s" style="border-bottom: none;">
			<h3 style="padding-left: 8px;">调整角色</h3>
		</div>
		<div class="sell-set-cont m-upgradeuserbox f-pt-m">
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx0" value="8" <#if agentRole==8>checked</#if> />
				<label for="radiofx0"></label>
			</div>
			<label for="radiofx0" class="f-mr-m font-s12">分享客</label>
			<div class="f-pt-xs"></div><!--/-->
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx1" value="5" <#if agentRole==5>checked</#if> />
				<label for="radiofx1"></label>
			</div>
			<label for="radiofx1" class="f-mr-m font-s12">分销 1 档</label>
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx2" value="6" <#if agentRole==6>checked</#if> />
				<label for="radiofx2"></label>
			</div>
			<label for="radiofx2" class="f-mr-m font-s12">分销 2 档</label>
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx3" value="7" <#if agentRole==7>checked</#if> />
				<label for="radiofx3"></label>
			</div>
			<label for="radiofx3" class="f-mr-m font-s12">分销 3 档</label>
			<div class="f-pt-xs"></div><!--/-->
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx4" value="1" <#if agentRole==1>checked</#if> />
				<label for="radiofx4"></label>
			</div>
			<label for="radiofx4" class="f-mr-m font-s12">代理 1 档</label>
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx5" value="2" <#if agentRole==2>checked</#if> />
				<label for="radiofx5"></label>
			</div>
			<label for="radiofx5" class="f-mr-m font-s12">代理 2 档</label>
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx6" value="3" <#if agentRole==3>checked</#if> />
				<label for="radiofx6"></label>
			</div>
			<label for="radiofx6" class="f-mr-m font-s12">代理 3 档</label>
			<div class="u-rb">
				<input type="radio" name="fenxiao" id="radiofx7" value="4" <#if agentRole==4>checked</#if>/>
				<label for="radiofx7"></label>
			</div>
			<label for="radiofx7" class="f-mr-m font-s12">代理 4 档</label>
		</div>
	</div>
	<div class="u-btn-box1 f-mt-m" style="padding-bottom: 0;">
		<input type="button" class="u-btn-mddkorg u-w dialog-save" value="保存" onclick="saveUserRole(${wxuserid!''})" />
	</div>
<script>
	function saveUserRole(userId){
        var agentRole = $("input[name='fenxiao']:checked").val();
		if(agentRole ==undefined ){
            errMsg("请选择角色!");
			return;
		}
        var url = CONTEXT_PATH + "/customer/updateUserRole/"+userId+"/"+agentRole;
        $.ajaxJsonGet(url,null,{
            done:function(res) {
            	if(res.data.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:res.data.msg,
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();

					if(agentRole==1){
						$("#roleName").text("代理商1档")
					}else if(agentRole==2){
                        $("#roleName").text("代理商2档")
					}else if(agentRole==3){
                        $("#roleName").text("代理商3档")
                    }else if(agentRole==4){
                        $("#roleName").text("代理商4档")
                    }else if(agentRole==5){
                        $("#roleName").text("分销商1档")
                    }else if(agentRole==6){
                        $("#roleName").text("分销商2档")
                    }else if(agentRole==7){
                        $("#roleName").text("分销商3档")
                    }else if(agentRole==8){
                        $("#roleName").text("分享客")
                    }

				}else{
                    errMsg(res.data.msg);
				}
            }
        });

	}

    /*错误提示*/
    function errMsg(msg) {
        var dm = new dialogMessage({
            type:2,
            title:'操作提醒',
            fixed:true,
            msg:msg,
            isAutoDisplay:false

        });
        dm.render();
    }
</script>