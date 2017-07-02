
		 <link rel="stylesheet" type="text/css" href="${basePath}/js/our-js/zxMsgBox/css/zxMsgBox.css"/>
			<script type="text/javascript">
				var basePath='${basePath}';
			</script>

	        <div class="main-content-pc" id="maindiv">
	            <div class="form-group col-xs-6 zhyh">
					<input type="text" class="form-control distance" placeholder="请输入员工手机号" id="phoneNumber" />	
					<div  class="btn btn-darkorange btn-radius-5" id="queryuser" > 员工查询  </div>
				</div>
				
				<div id="userDiv"></div>	
				
				<div class="jm-font-ming-md">员工总人数：${count} 人</div>
				<div class="jm-table" >
					<ul class="table-hander">
						<li>头像</li>
						<li>姓名</li>
						<li>手机号</li>
						<li>角色</li>
						<li>身份</li>
						<li style="width:15%;">管理</li>
					</ul>
					<#list ulist as item>
					<ul class="table-container">
						<li>
							<div class="table">
								<div class="table-cell vertical-middle" style="width:0%;">
									<div class="checkBox">
									<input type="checkbox" name="" id="checkbox2" value="" />
									 <label class="iconfont icon-avoid"  for="checkbox2"></label>
									</div>
									
								</div> 
								<div class="table-cell vertical-middle">
								  <a href="#" class="thumbnail">
									<img src="${basePath}/css/pc/img/no_picture.png" alt="暂无图片" class="avt">
								  </a>
								</div>
							</div>
						</li>
						<li>${item[1]}</li>						
						<li>${item[2]}</li>
						<li>${item[3]}</li>
						<li>聚米注册用户</li>	
						<li style="width:15%;">
							<div  class="btn  btn-lightgray btn-sm dialog" onclick="dialoga(${item[0]})"> 更改授权</div>
							
							<div  class="btn  btn-lightgray btn-sm" id="delrole" data="${item[4]}"> 删除 </div>
						</li>
					</ul>
					</#list>
					
				</div>
			</div>
			
			<div id="dialoginfo" class="dialoginfo dhk">
				<div class="select">
					 <#assign i=1>
					<#list zbRole as role1>
					<div>
						<span>${role1.roleName}</span>
						<input type="radio" name="roleId" value="${role1.roleId}"  />
					</div>
					 <#assign i++>
					</#list>
					</div>
			</div>
		
			<script src="${basePath}/js/busi-js/pc/system/userlist.js" type="text/javascript" charset="utf-8"></script>
