<!-- 上下文根路径-->
<script type="text/javascript" charset="utf-8">
    var CONTEXT_PATH = '${basePath}';
    var STATIC_URL = '${STATIC_URL}';
    var TPL_CACHE = '${TPL_CACHE}';
    var DOMAIN = '${DOMAIN}';
    var THIRD_URL = '${THIRD_URL}';
    var COMPRESS = '${COMPRESS}';
    var PLAT_FORM = '${PLAT_FORM}';
</script>

<script src="${THIRD_URL}/third/jquery/jquery-1.11.0.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/jquery/jquery-ui-1.12.0/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
<!--框架 -->
<script src="${THIRD_URL}/third/framework/vue-1.0.28/vue.min.js" type="text/javascript" charset="utf-8"></script>
<!--时间组件start-->
<script src="${THIRD_URL}/third/jquery/datetimeaddon/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/jquery/datetimeaddon/js/jquery-ui-timepicker-addon.js" type="text/javascript" charset="utf-8"></script>
<!--文本编辑器-->
<script src="${THIRD_URL}/third/util/uedit/ueditor.config.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/util/uedit/ueditor.all.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/util/echarts/echarts.min.js"></script>

<#if COMPRESS=='1'>
<script src="${STATIC_URL}/base.min.js" type="text/javascript" charset="utf-8"></script>
<#else>

<script src="${THIRD_URL}/third/jquery/jquery.md5.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/util/select2/js/select2.min.js" type="text/javascript" charset="utf-8"></script>
<!--复制-->

<script src="${THIRD_URL}/third/util/clipboard.js-master/dist/clipboard.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/jquery/jquery.validate/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>


<!-- 分页插件 -->
<script src="${THIRD_URL}/third/jquery/pagination/jquery.pagination.js" type="text/javascript" charset="utf-8"></script>
<!--select2组件-->
<script src="${THIRD_URL}/third/util/select2/js/i18n/zh-CN.js" type="text/javascript" charset="utf-8"></script>
<!-- 弹窗插件 -->
<script src="${THIRD_URL}/third/util/artDialog/js/dialog.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/util/artDialog/js/dialog-plus.js" type="text/javascript" charset="utf-8"></script>

<!--framework -->
<script src="${THIRD_URL}/third/framework/nunjucks/nunjucks.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/framework/nunjucks/nunjucks-extends.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/framework/underscore/underscore.js" type="text/javascript" charset="utf-8"></script>


<!--上传图片插件-->
<script src="${THIRD_URL}/third/jquery/fileupload/jquery.fileupload.js"></script>
<script src="${THIRD_URL}/third/jquery/jquery.lazyload.js" type="text/javascript" charset="utf-8"></script>
<!--表单验证组件-->

<script src="${THIRD_URL}/third/jquery/jquery.validate/jquery.validate.extend.js" type="text/javascript" charset="utf-8"></script>

<!--树插件-->
<script src="${THIRD_URL}/third/jquery/ztree/v3.3/js/jquery.ztree.core-3.5.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/third/jquery/ztree/v3.3/js/jquery.ztree.excheck-3.5.js" type="text/javascript" charset="utf-8"></script>
<#--省市区插件-->
<script src="${THIRD_URL}/third/util/distpicker/distpicker.data.js"></script>
<script src="${THIRD_URL}/third/util/distpicker/distpicker.js"></script>
<!--报表-->
<script src="${THIRD_URL}/third/util/echarts/china.js"></script>
<!-- web im start-->
<script src="${THIRD_URL}/third/util/web-im/static/sdk/strophe.js"></script>
<script src="${THIRD_URL}/third/util/web-im/static/sdk/easemob.im-1.1.1.js"></script>
<script src="${THIRD_URL}/third/util/web-im/static/js/swfupload/swfupload.js"></script>
<script src="${THIRD_URL}/third/util/web-im/static/js/easemob.im.config.js"></script>
<script src="${THIRD_URL}/third/util/web-im/static/sdk/easemob.im.shim.js"></script>
<script src="${THIRD_URL}/third/util/SuperSlide.2.1.1.js"></script>

<script src="${THIRD_URL}/our/js/util/jmClient.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/our/js/util/common.js" type="text/javascript" charset="utf-8"></script>
    <#--<#if PLAT_FORM ?? && PLAT_FORM=='0'>-->
        <script src="${THIRD_URL}/our/js/util/jumi.js" type="text/javascript"></script>
    <#--<#else>-->
    <#--<script src="${STATIC_URL}/js/busi-js/pc/zb/jumi.js" type="text/javascript" charset="utf-8"></script>-->

    <#--</#if>-->

<script src="${THIRD_URL}/our/js/util/dialog_message.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/our/js/util/map_material.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/our/js/util/jm-ui.js" type="text/javascript" charset="utf-8"></script>

<script src="${THIRD_URL}/our/js/common/region.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/our/js/common/task.js" type="text/javascript" charset="utf-8"></script>
<!-- 地区下拉专用js -->
<script src="${THIRD_URL}/our/js/util/areaCommon.js" type="text/javascript"></script>
<script src="${THIRD_URL}/our/js/util/addCustomizeButton.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/our/js/common/baidu_map_readdr.js" type="text/javascript" charset="utf-8"></script>
<script src="${THIRD_URL}/our/js/common/hotSpotCustomize.js" type="text/javascript" charset="utf-8"></script>
<#--图片弹窗js-->
<script src="${THIRD_URL}/third/util/lightbox/lightbox.js" type="text/javascript" charset="utf-8"></script>

</#if>
