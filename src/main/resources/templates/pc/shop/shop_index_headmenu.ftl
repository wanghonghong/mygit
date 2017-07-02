<section class="g-mn">
    <div class="u-logo" id="u-logo">
        <img src="${THIRD_URL}/css/pc/img/logo1.png" />
    </div>
    <div class="m-nav" >
        <div class="g-fixed-memu ">
            <#list leftResources as item>
                <nav id="resourceD${item.resourceId!''}">
                    <a href="javascript:void(0);" data-tpl-id="${item.resourceId!''}" data-tpl-name="${item.tplName!''}" data-tpl-url="${item.url!''}" data-id="${item.resourceId!''}"  onclick="toTpl(this)" >${item.resourceName!''}</a>
                </nav>
            </#list>
        </div>
        <div class="g-float-memu">
            <div class="othermemu" id="othermemu">
                <div>
                    <#list rightResources1 as item>
                        <nav id="resourceD${item.resourceId!''}">
                            <a href="javascript:void(0);" data-tpl-id="${item.resourceId!''}" data-tpl-name="${item.tplName!''}" data-tpl-url="${item.url!''}" data-id="${item.resourceId!''}" onclick="toTpl(this)" >${item.resourceName!''}</a>
                        </nav>
                    </#list>
                </div>
                <div>
                    <#list rightResources2 as item>
                        <nav id="resourceD${item.resourceId!''}">
                            <a href="javascript:void(0);" data-tpl-id="${item.resourceId!''}" data-tpl-name="${item.tplName!''}" data-tpl-url="${item.url!''}" data-id="${item.resourceId!''}" onclick="toTpl(this)" >${item.resourceName!''}</a>
                        </nav>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</section>
