<html>
<title>授权成功</title>
<head>
<#include "/pc/common/common_css.ftl" />
    <style>
        body{ text-align:center}
        .div1{
            width: 400px;
            height: 200px;
            position: absolute;
            text-align: center;
            left:0;
            right:0;
            top: 0;
            bottom: 0;
            margin: auto;
            /*50%为自身尺寸的一半*/
        }
    </style>
</head>
<body>
<div class="div1">
<div class="m-sus-box">
    <img class="f-fl" src="http://dev.jumiweigu.com/img/pc/jmtool22.png" />
    <span class="f-pt-m">
					恭喜您，授权成功<br>
    </span>

</div>
<div class="u-btn-box1">
    本页将在 <font id="jumpTo">5</font> 秒后<a href="${basePath}/shop"> 进入首页</a>
</div>
</div>
<#--授权成功，本页将在5秒后跳转！-->
</body>

<script language="javascript">
    function clock(){
        //document.title="本页面将在"+i+"秒后跳转!";
        document.getElementById('jumpTo').innerHTML = i;
        if(i==0){
            clearTimeout(st);
            //window.opener=null;
            //window.close();
            var CONTEXT_PATH = '${basePath}';
            window.location.href=CONTEXT_PATH+"/shop";
        }
        i = i -1;
        st = setTimeout("clock()",1000);
    }
    var i=5 ;
    clock();
</script>
</html>