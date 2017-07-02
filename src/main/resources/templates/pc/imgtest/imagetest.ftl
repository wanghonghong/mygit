<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>图片上传测试</title>
		<!-- <script type="text/javascript" src="${basePath1}/js/third-js/jquery-1.11.0.js"></script>
		<script type="text/javascript" src="${basePath1}/js/our-js/ajaxfileupload.js"></script>
		<script type="text/javascript" src="${basePath1}/js/busi-js/pc/product/testImg.js"></script>
		<script src="${basePath}/js/our-js/common.js" type="text/javascript" charset="utf-8"></script> -->
	</head>
	
	<body>
		
		图片上传
		<div style="height: 120px;">
			<!-- <img src="${THIRD_URL}/img/pc/upload/35143.775373189122.png" id="showImg" alt="" width="360px"
				height="115px" style="display: display" /> -->
			<img src="" id="showImg" name="showImg" alt="" width="360px"
				height="115px" />
		</div>
		<div>
			<input type="file" id="myfile" name=myfile onchange="fileuplod()"/> 
			
			<input type="hidden" id="fileName" name="fileName">
		</div>
		<span style="color: red;">（限传一张，推荐尺寸720*225像素，大小不能超过512K）</span>
</body>

</html>