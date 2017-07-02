<style>
    .left-memu-pc {
        float: left;
        width: 186px;
    }
</style>
		<#include "/pc/shop/shop_index_head.ftl" />
        <div class="g-content" >
			<#--左边菜单-->
            <div class="g-sd-left" id="shopIndexLeftmenu" style="display: none;">
                <div class="m-memu" id="leftMenuUl">

                </div>
            </div>
			<#--主内容-->
            <div id="shopIndexContent" class="gmn" >

            </div>
        </div>
        <script>
            shop.indexMenu.init();
        </script>
		<#include "/pc/shop/shop_index_bottom.ftl" />