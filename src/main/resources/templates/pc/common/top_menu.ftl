		<input type="hidden" name="shopId" id="shopId" value="${jmuser.shopId!}"/>
        <input type="hidden" name="shopName" id="shopName" value="${jmuser.shopName!}"/>


        <!-- 头部模块 -->
        <header class="g-hd" id="jumiheader">
            <header>
                <div class="g-mn">
                    <label>欢迎来到聚米为谷！</label>
                    <div class="g-memu">
                     <#if  jmuser.imgUrl ?? && jmuser.imgUrl!=''  >
                        <img src="${jmuser.imgUrl!}"/>
                      <#else >
                       <img src="${THIRD_URL}/css/pc/img/logo.png"/>
                        </#if>
                        <div>
                            商家店铺
                            <i class="iconfont icon-belowtriangle"></i>
                        </div><br>
                       <#-- <ul >
                            <li> <a href="${STATIC_URL}/shop">店铺管理</a> </li>
                            <li> <a href="${STATIC_URL}/jm_index">个人资料</a> </li>
                            <li> <a href="http://jumiweigu.com/" target="_blank">官网</a> </li>
                            <li> <a href="${STATIC_URL}/loginout">退出</a> </li>
                        </ul>-->

                         <ul>
                             <a href="${basePath}/shop">
                                 <li>店铺管理</li>
                             </a>
                             <a href="${basePath}/shop#3">
                                 <li>个人资料</li>
                             </a>
                             <a href="http://jumiweigu.com/" target="_blank">
                                 <li>官网</li>
                             </a>
                             <a href="${basePath}/loginout">
                                 <li>退出</li>
                             </a>
                         </ul>

                    </div>
                </div>
            </header>
            <section class="g-mn">
                <div class="u-logo">
                    <a href="${basePath}/shop">
                        <img src="${THIRD_URL}/css/pc/img/logo1.png" />
                    </a>
                </div>
                <div class="m-nav">
                    <ul  >

                    </ul>

                </div>

            </section>
        </header>





		<#--<div class="hander-pc">-->
		    <#--<div class="hander-top">-->
			  <#--<div class="content-pc">-->
			  	<#--<label>欢迎来到聚米为谷！</label>-->
				  	<#--<div class="top-memu">-->
				  		  <#--<img src="${THIRD_URL}/img/pc/shopimg2.png" /> -->
				  		<#--<span>商铺切换 -->
				  			<#--<i class="iconfont icon-belowtriangle"></i>-->
				  		<#--</span><br>-->
				  		<#--<ul class="zbUser-memu-ul">-->
				  			<#--<li> <a href="${basePath}/shop">店铺切换</a> </li>-->
				  			<#--<li> <a href="">商铺设置</a> </li>-->
				  			<#--<li> <a href="">个人资料</a> </li>-->
				  			<#--<a href="${basePath}/loginout"><li> 退出 </li></a>-->
				  		<#--</ul>-->
				  	<#--</div>-->
		    	<#--</div>-->
			<#--</div>-->
			<#---->
			<#--<div class="hander-middle">-->
				  	<#--<div class="logo-region">-->
				  		<#--<a href="${basePath}/shop">-->
				  		<#--<img src="${THIRD_URL}/css/pc/img/logo.png" class="logo-img"/>-->
				  		<#--</a>-->
				  	<#--</div>-->
				  	<#--<div class="memu-region">-->
				  		 <#--<ul class="memu-li">-->
				  		 <#---->
				  		 <#--</ul>-->
				  	<#--</div>-->
			<#--</div>-->
			<#---->
		<#--</div>-->
		<#---->
		
		
