<#if PLAT_FORM ?? && PLAT_FORM=='0'>
    <#include "/pc/common/head.ftl" />
    <#include "/pc/common/common_css.ftl" />
    <#include "/pc/common/top_menu.ftl" />
<!-- 主界面 -->
<div class="g-content">
    <#include "/pc/common/left_menu.ftl" />
    <!--主模块-->
    <section class="g-mn" id="shop_content">
    </section>
    <!--/主模块-->
    <div id="webim_chat" class="hide  ui-widget-content" style="height:0px;width:auto; border: none;"></div>
</div>

<#else>

    <#include "/pc/common/head.ftl" />
    <#include "/pc/common/common_css.ftl" />
    <#include "/pc/common/zb_top_menu.ftl" />

<div class="g-content">
    <div id="leftmenu">
        <!-- 左侧菜单 -->
        <#include "/pc/common/left_menu.ftl" />
        <!-- 左侧菜单 -->
    </div>
    <!--主模块-->
    <section class="g-mn" id="shop_content">
    <#--<div class="g-wrap-box-xyh" id="content">-->
        <#--</div>-->
    </section>
    <!--/主模块-->
</div>
</#if>

<!-- /主界面 -->
<#include "/pc/common/common_js.ftl" />
<#include "/pc/common/footer.ftl" />

<script>
    frame.init();
</script>

