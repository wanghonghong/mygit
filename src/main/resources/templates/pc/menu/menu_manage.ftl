<link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/phone-shell.css"/>
<style>
    .link-tag-add{
        font-size: 12px;
    }
    /*@leixy*/
    .menu-settings .menu-settings-panel .setting-memu .link-set-group .link-set{
        line-height: 33px;
    }
</style>

<div class="m-tab f-mb-m">
    <ul id="memutab">
        <li class="z-sel" data-target="memusetBox">菜单设置</li>
        <li class="" data-target="authorizeBox">微信授权</li>
    </ul>
</div>
<div>
    <div id="memusetBox">
        <div class="row clearfix">
            <div class="col-xs-4" style="margin-right: -15px;">
                <div class="phone-view" style="width: 330px;">
                    <div class="phone-shell-top" style="height: 62px;">
                    </div>
                    <div class="phone-window">
                        <div id="app-main-content">
                        </div>
                        <div id="app-memu-panel">
                            <div class="keyboard-icon">
                                <i class="iconfont icon-keyboard"></i>
                            </div>
                            <ul class="app-bottom-memu" id="app-bottom-memu">
                            </ul>
                        </div>
                    </div>
                    <div class="phone-shell-bottom">
                    </div>
                </div>
            </div>
            <div class="col-xs-7 menu-settings" style="padding-left: 0;">
                <div class="one-level-add">
                    <a class="btn  btn-orange-font  btn-lg btn-radius-5   "><i
                            class="iconfont icon-add"></i><span>一级菜单</span></a>
                </div>
            </div>
        </div>
        <div class="u-btn-box1">
            <input type="button" class="u-btn-mainlg" id="nextStep" value="保存" />
        </div>
        <#--<div class="opt-btn-panel">-->
            <#--<input type="button" value="保存"-->
                   <#--class="btn   btn-lightorange  btn-lg btn-radius-5 col-xs-offset-5 radius-5 "-->
                   <#--id="nextStep"/>-->
        <#--</div>-->
        <div class="clone-div" style="display: none;">
            <div class="menu-settings-panel">
                <div class="one-level setting-memu">
                    <div class="col-xs-5 col-ms-12">
                        <label>1</label>
                        <input type="text" class="memu-name c-gray1" placeholder="请输入一级菜单名称"/>
                    </div>
                    <div class="col-xs-7 col-ms-12">
                        <div class="link-set-group">
                            <div class="link-set" style="background-color: #d5d5d5;"><i class="iconfont icon-link "></i><span class="font-s12">点击设置</span></div>
                            <input type="text" class="link-input" placeholder="输入其他网址"/>
                        </div>
                        <i class="iconfont icon-delete1 f-ml-xs" style="color: #b9b9b9;"></i>
                    </div>
                </div>
                <div class="u-t3border bg-white f-pb-xs">
                <div class="two-level-panel">

                </div>
                <div class="two-level-add">
                    <div class="btn u-btn-lgltgry" style="padding: 0 25px; font-size: 14px; margin-left: 40px;"><i class="iconfont icon-add"></i><span>二级菜单</span></div>
                    <#--<a class="btn   btn-lightgray  btn-md btn-radius-5  btn-border-lightgray"><i-->
                            <#--class="iconfont icon-add"></i><span>二级菜单</span></a>-->
                </div>
                </div>
            </div>
            <div class="two-level setting-memu">
                <div class="col-xs-5 col-ms-12">
                    <label style="color: #949494;">菜单1</label>
                    <input type="text" class="memu-name c-gray1" placeholder="请输入一级菜单名称"/>
                </div>
                <div class="col-xs-7 col-ms-12">
                    <div class="link-set-group">
                        <div class="link-set" style="background-color: #bbc4cb;"><i class="iconfont icon-link "></i><span class="font-s12">点击设置</span></div>
                        <input type="text" class="link-input" placeholder="输入其他网址"/>
                    </div>
                    <i class="iconfont icon-jianhao f-ml-xs" style="color: #c9c9cb;"></i>
                </div>
            </div>
            <div class="memudialog" id="memudialog"></div>
        </div>

        <div id="dialoginfo" class="dialoginfo dhk">
            <div class="select dialog-js">
                <div class="dialog-jstxt1"><span>根据您所经营行业，系统已将推荐使用的商城主题模板推送到您公众号，请打开公众号查看!</span></div>
                <div class="dialog-jsimg"></div>
                <div class="dialog-jstxt2">因公众号可能授权延时，建议您先取消公众号关注，再关注，便可快速看到公众号新菜单、主题商城。</div>
                <div class="dialog-jstxt3">如需要更换商城主题，请关闭本页后，重新选择主题模板！</div>
            </div>
        </div>
    </div>
    <div id="authorizeBox" class="z-hide">

        <div class="g-addshopbox">
            <div class="g-addshop f-pt-s">
                <div class="col-xs-12 f-tc f-mb-xl">
                    <div class="u-wx-point f-mb-l">
                        <p>绑定微信公众号，把店铺和微信打通</p>
                        <p>绑定后即可在这里管理您的公众号，聚米为谷提供比微信官方更强大的功能</p>
                    </div>
                    <a href="${basePath}/auth/page1" target="_blank">
                        <div class="u-btn-weixin">
                            <i class="iconfont icon-wechat"></i>再次授权<i class="iconfont icon-ensure"></i>
                        </div>
                    </a>
                </div>
                <dl class="col-xs-11 u-wx-explain f-mt-xl f-mb-xxl f-ml-xl u-noborder">
                    <dt style="font-size: 14px;"><i class="iconfont icon-lamp"></i>温馨说明：</dt>
                    <dd>1、本次授权绑定未成功，可能因各种原因产生绑定失败，请自行排查！查看绑定失败原因请点击　　<a href="#">查看失败原因</a></dd>
                    <dd>2、如各种原因所描述问题均不存在，可能因系统缓冲原因产生绑定失败，请点击上方再次授权！</dd>
                    <dd>3、公众号授权绑定成功，您所新建的店铺才能启用，您方可进入聚米为谷为你打造的移动电商系统！</dd>
                </dl>
            </div>
        </div>
    </div>
</div>
<div id="link_content" style="display: none;"></div>
<script src="${STATIC_URL}/js/busi-js/pc/menu/memu-settings.js"></script>

<script type="text/javascript">
    var prefixUrl = "${domain}";

    function showRoleWindow() {
        var elem = document.getElementById('dialoginfo');
        dialog({
            title: "角色选择",
            content: elem,
            okValue: '我知道了',
            ok: function () {
            },
        }).width(500).height(280).showModal();

    }
    function goAuthpage() {
        var authUrl="${basePath}/auth/page1";
        $.ajax({
            type : 'GET',
            url: authUrl,
            success : function(data) {
                window.open(data);
            }
        });
    }
    $(function () {
        //showRoleWindow();
        $("#memutab li").click(function(){
            var _this= $(this);
            _this.addClass("z-sel").siblings().removeClass("z-sel");
            var target = _this.attr("data-target");
            $("#"+target).removeClass("z-hide").siblings().addClass("z-hide");
            console.log( $("#"+target).children().length);
        })

    });
</script>
	
	
