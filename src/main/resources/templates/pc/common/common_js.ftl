<#include "/pc/common/base_js.ftl" />

<#if COMPRESS=="1">
    <script src="${STATIC_URL}/busi.min.js" type="text/javascript" charset="utf-8"></script>
    <#--<script src="${STATIC_URL}/js/busi-js/pc/product/product_details.js" type="text/javascript" charset="utf-8"></script>-->
<#else>
    <#include "/pc/common/busi_js.ftl" />
</#if>

<!--百度地图接口  注：放置倒数第一位，避免影响其它脚本执行 -->
<script src="https://api.map.baidu.com/api?v=2.0&ak=9sPn7VNhii3EV9LcjRfFPXNAqGxt7uA3&s=1" type="text/javascript"></script>