<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title></title>
    <!- -[if (gte IE 6)&(lte IE 8)]>
    <![endif]- ->

      <!--   <link rel="stylesheet" type="text/css" href="${THIRD_URL}/csscss/dialog-box.css"/> -->
        
	</head>
	<body>
		
		<div id="dialoginfo" class="dialoginfo dialoginfo-lstk">
					<div  class="dialog-title">
						<div class="tab lstk-tab">
					      <ul>
						  <li class="active">本地图库</li>
						  <li>历史图库</li>
						  <i class="iconfont icon-delete1"></i>
						  
					      </ul>			      
					    </div>					
						
					</div>
					<div id="content">
						<div class="stock-info-details-right dialog-js" id="dialog_0">
						<div class="stock-info-details">
							<div class="">
								<div class="stock-info-details-add-img text-center">
									<div>
										<div>
											<i class="iconfont icon-add"></i>
											<input type="file">	
										</div>
									</div>
											
								</div>												
							</div>											
						</div>	
						
						<div class="dialog-jstxt3 text-center">温馨提示：图片命名以便于在历史图库中搜索查找</div>
						<div class="dialog-jstxt3 text-center">仅支持jpg、gif、png三种格式, 大小不超过1 MB</div>
					</div>
					<div id="dialog_1" class="existing-classify-choose padding-l disloge" style="display:none;">
						<ul class="clearfix">
					<#if shopResourceList??>
					     <#list shopResourceList as zbShopResource>
							<li class="floatleft">
								<div class="existing-classify-info-lst lst-img">
								<div class="goods-classify-img">
						        	<img width="100" src="${basePath}${zbShopResource.resUrl}" value="" />
						       	</div>					        			       
								</div>
								<div class="lstk-tittle">
									<div class="checkBox" style="border:none;">
										<input type="hidden" name="mapid" value="${zbShopResource.id}"  />
										<input type="radio" name="map" id="${zbShopResource.id}" value="${zbShopResource.resUrl}" />
										<label class="iconfont icon-avoid" style="border:none;" for="${zbShopResource.id}"></label>
									</div>	
									
								</div>
								
								<div  class="btn  btn-lightgray btn-sm lstk-btn"> 删除 </div>	
							</li>
						</#list>
				</#if>
						</ul>
						
						
					</div>	
								
					
		</div>
		<div id="page">
						
	    </div>
		
	     
	   	<script>
	   
	   	
	   	</script>
	</body>

</html>