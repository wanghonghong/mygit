<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>新增店铺</title>
    <link rel="stylesheet" href="${THIRD_URL}/css/pc/index.css">
    <title></title>
    <link rel="stylesheet" type="text/css" href="${THIRD_URL}/css/pc/dialog-person.css"/>

    <link rel="stylesheet" type="text/css" href="${basePath}/js/third-js/artDialog/css/ui-dialog.css"/>
    <script type="text/javascript">
        var CONTEXT_PATH='${basePath}';
        var basePath1='${basePath}';
        var authPageUrl='${authPageUrl}';
    </script>
</head>

<body>
<!-- 头部模块 -->
<header class="g-hd s-white">
    <header>
        <div class="g-mn ">
            <label>欢迎来到聚米为谷！</label>
            <div class="g-memu">
                <img src="${THIRD_URL}/css/pc/img/logo1.png" />
                <div>
                    商家平台
                    <i class="iconfont icon-belowtriangle"></i>
                </div><br>
                <ul>
                    <a href="${basePath}/shop">
                        <li>店铺管理</li>
                    </a>
                    <a href="${basePath}/jm_index">
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
        <div class="u-logo" id="u-logo">
            <img src="${THIRD_URL}/css/pc/img/logo1.png" />
        </div>
        <div class="m-nav" >
            <div class="g-fixed-memu ">
                <nav >
                    <a href="${basePath}/jm_index">个人资料</a>
                </nav>
                <nav class="z-sel">
                    <a href="${basePath}/shop" >店铺管理</a>
                </nav>
            </div>
            <div class="g-float-memu">
                <div class="othermemu" id="othermemu">
                    <div >
                        <nav>
                            <a href="${basePath}/software_agent/apply">软件代理平台</a>
                        </nav>
                        <nav >
                            <a href="${basePath}/operation/apply">运营服务平台</a>
                        </nav>
                        <nav>
                            <a href="#">图文手创平台</a>
                        </nav>
                        <nav>
                            <a href="#">O2O媒体流量平台</a>
                        </nav>
                    </div>
                    <div >
                        <nav>
                            <a href="#">聚高手</a>
                        </nav>
                        <nav>
                            <a href="#">聚社区</a>
                        </nav>
                        <nav>
                            <a href="#">聚异堂</a>
                        </nav>
                        <nav>
                            <a href="#">聚学院</a>
                        </nav>
                        <nav>
                            <a href="#">聚青创</a>
                        </nav>
                    </div>

                </div>
            </div>
        </div>
    </section>
</header>
<!-- /头部模块 -->
<!-- 主界面 -->
<div class="g-content">
    <!--主模块-->
    <section>

        <div id="div1">
            <div class="g-addshopbox f-mt-xl">
                <div class="g-addshop f-pt-s">
                    <div class="col-xs-12 f-mb-l">
                        <ul class="m-step-bar-box">
                            <li class="first-step current-step">
                                <label><span>1</span><i class="iconfont icon-avoid"></i></label>
                                <h5>新增店铺资料</h5>
                            </li>
                            <li class="last-step">
                                <label><span>2</span><i class="iconfont icon-avoid"></i></label>
                                <h5>微信公众号授权</h5>
                            </li>
                        </ul>
                    </div>
                    <div class="col-xs-12" style="margin-bottom: 10px">
                        <label class="u-lb-md c-black">店铺名称</label>
                        <div class="u-txt col-xs-9">
                            <input type="hidden" id="shopId" name="shopId" value="${shop.shopId!''}" />
                            <input type="text" class="ipt-txt" placeholder="请输入店铺名称"  id="shopName" name="shopName" value="${shop.shopName!''}" />
                        </div>
                        <label class="u-lb-md c-black">主营商品</label>
                        <div class="u-txt col-xs-9">
                            <div class="ipt-txt c-gray1 u-bombbox-box">

                                <input type="hidden" id="typeId" value="${shop.shopType!''}" />
                                <span id="prname">${catename!'请选择主营商品'}<i class="iconfont icon-down"></i></span>
                                <ul class="u-bombbox" id="prtype">
                                    <#list prls as pr>
                                        <li data-id="${pr.typeId!''}">${pr.typeName!''}</li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                        .
                        <label class="u-lb-md c-black">店铺地址</label>
                        <div id="distpicker" >
                            <div class="u-txt col-xs-3">
                                <select class="ipt-txt c-gray1" id="province" >
                                    <option>省</option>
                                </select>
                            </div>
                            <div class="u-txt col-xs-3">
                                <select class="ipt-txt c-gray1" id="city" >
                                    <option>市</option>
                                </select>
                            </div>
                            <div class="u-txt col-xs-3">
                                <select class="ipt-txt c-gray1"  id="district">
                                    <option>区</option>
                                </select>
                            </div>
                        </div>

                        <label class="u-lb-md c-black">详细地址</label>
                        <div class="u-txt col-xs-9">
                            <textarea class="ipt-txt txtarea" placeholder="请输入详细地址" id="specificAddr" name="specificAddr" >${shop.specificAddr!''}</textarea>
                        </div>
                    </div>

                </div>
            </div>
            <div class="u-btn-box1">
                <div class="u-cb bg-white">
                    <input type="checkbox" name="quota" id="checkBox1" value=""/>
                    <label class="iconfont icon-avoid" for="checkBox1"></label>
                </div>
                <label for="checkBox1">我已阅读并同意<font class="c-gray1">《聚米为谷商家版代理销售服务和结算协议、担保交易服务协议》</font></label>
            </div>

            <div class="u-btn-box"><input type="button" class="u-btn-lgdkorg" value="下一步" id="addshop" /></div>
        </div>


        <div id="div2"  style="display:none"  >
            <div class="g-addshopbox f-mt-xl">
                <div class="g-addshop f-pt-s">
                    <div class="col-xs-12 f-mb-xxl">
                        <ul class="m-step-bar-box">
                            <li class="first-step">
                                <label><span>1</span><i class="iconfont icon-avoid"></i></label>
                                <h5>新增店铺资料</h5>
                            </li>
                            <li class="last-step current-step">
                                <label><span>2</span><i class="iconfont icon-avoid"></i></label>
                                <h5>微信公众号授权</h5>
                            </li>
                        </ul>
                    </div>
                    <div class="col-xs-12 f-tc f-mb-xl">
                        <div class="u-wx-point f-mb-l">
                            <p>绑定微信公众号，把店铺和微信打通</p>
                            <p>绑定后即可在这里管理您的公众号，聚米为谷提供比微信官方更强大的功能</p>
                        </div>
                        <div class="u-btn-weixin"  id="show-qrcode1" ><i class="iconfont icon-wechat"></i>我有微信公众号，立即授权<i class="iconfont icon-ensure"></i></div>
                        <div class="u-btn-weixin" id="show-qrcode2" style="display: none" ><i class="iconfont icon-wechat" ></i>再次授权<i class="iconfont icon-ensure"></i></div>
                    </div>
                    <dl class="col-xs-11 u-wx-explain f-mb-l" id="aginDl1">
                        <dt>温馨说明：</dt>
                        <dd>1、公众号需为认证期内的服务号，非此类公众号无法调用聚米为谷商城系统使用；请注意公众号每年年检正常，合格公众号请点击　　<a href="#">如何查看</a></dd>
                        <dd>2、授权绑定成功后，聚米为谷将根据您经营的行业和商品，自动推送推荐使用的公众号菜单替换您原有的公众号菜单设置；请确认您现有公众号菜单可替换再进行重新授权！</dd>
                        <dd>3、授权绑定成功后，您便可以在店铺管理里管理您的公众号和商城系统，聚米为谷为您提供强大的功能及性能保障！</dd>
                        <dd>4、使用聚米为谷微信商场系统，请勿频繁授权给其他第三方微信商城系统，以便保障您日常正常使用及客户交易安全！</dd>
                        <dd>5、公众号授权绑定成功，您所新建的店铺才能启用，您方可进入聚米为谷为你打造的移动电商系统！</dd>
                        <dd>6、公众授权成功，店铺即创建成功，请注意店铺商城的支付设置！</dd>
                    </dl>
                    <dl class="col-xs-11 u-wx-explain f-mb-l" id="aginDl2" style="display: none">
                        <dt>温馨说明：</dt>
                        <dd>1、本次授权绑定未成功，可能因各种原因产生绑定失败，请自行排查！查看绑定失败原因请点击　　<a href="#">查看失败原因</a></dd>
                        <dd>2、如各种原因所描述问题均不存在，可能因系统缓冲原因产生绑定失败，请点击上方再次授权！</dd>
                        <dd>3、公众号授权绑定成功，您所新建的店铺才能启用，您方可进入聚米为谷为你打造的移动电商系统！</dd>
                    </dl>
                </div>
            </div>
            <div class="u-btn-box">
                <input type="button" class="u-btn-lgdkorg" value="上一步" name="topbtn"  />
            </div>
        </div>



        <div id="div3"  style="display:none"  >
            <div class="g-addshopbox f-mt-xl">
                <div class="g-addshop f-pt-s">
                    <div class="col-xs-12 f-mb-xxl">
                        <ul class="m-step-bar-box">
                            <li class="first-step">
                                <label><span>1</span><i class="iconfont icon-avoid"></i></label>
                                <h5>新增店铺资料</h5>
                            </li>
                            <li class="last-step current-step">
                                <label><span>2</span><i class="iconfont icon-avoid"></i></label>
                                <h5>微信公众号授权</h5>
                            </li>
                        </ul>
                    </div>
                    <div class="col-xs-12 f-tc f-mb-xl">
                        <div class="u-wx-point f-mb-l">
                            <p>绑定微信公众号，把店铺和微信打通</p>
                            <p>绑定后即可在这里管理您的公众号，聚米为谷提供比微信官方更强大的功能</p>
                        </div>
                        <div class="u-btn-weixin"><i class="iconfont icon-wechat"></i>再次授权<i class="iconfont icon-ensure"></i></div>
                    </div>
                    <dl class="col-xs-11 u-wx-explain f-mb-l">
                        <dt>温馨说明：</dt>
                        <dd>1、本次授权绑定未成功，可能因各种原因产生绑定失败，请自行排查！查看绑定失败原因请点击　　<a href="#">查看失败原因</a></dd>
                        <dd>2、如各种原因所描述问题均不存在，可能因系统缓冲原因产生绑定失败，请点击上方再次授权！</dd>
                        <dd>3、公众号授权绑定成功，您所新建的店铺才能启用，您方可进入聚米为谷为你打造的移动电商系统！</dd>
                    </dl>
                </div>
            </div>
            <div class="u-btn-box">
                <input type="button" class="u-btn-lgdkorg" value="上一步" name="topbtn"  />
                <input type="button" class="u-btn-lgdkorg" value="完成" name="okbtn" />
            </div>
        </div>

    </section>
    <!--/主模块-->
</div>
<!-- /主界面 -->
<!-- 底部模块 -->
<footer class="g-ft s-white ">
    <label> Copyright  版权由聚米为谷科技所有 </label>
</footer>
<!-- /底部模块 -->
<div id="dialoginfo" class="dialoginfo">
    <div class="m-appling">
        <h3>你已成功提交申请</h3>
        <img src="${THIRD_URL}/img/pc/jmtool2.png"  />
        <p>三个工作日内，工作人员将与您电话确认，敬请保持手机接听畅通</p>
        <label>聚米为谷全国统一服务电话：<span>400-0766-568</span></label>
        <div class="u-btn-box1 f-mt-m"><input type="button" class="u-btn-mddkorg" value="我知道了" /></div>
    </div>
</div>
<div id="dialoginfo1" class="dialoginfo dhk" style="display: none;">
    <div class="select dialog-js login-tc">
        <div class="dialog-jsimg"></div>
        <div class="dialog-jstxt1 text-center"><span class="login-tis">在新窗口中完成微信公众号授权</span></div>
    </div>
</div>
<div id="dialoginfo2" class="dialoginfo m-wx-sus">
    <div class="m-sus-box">
        <img class="f-fl" src="${THIRD_URL}/img/pc/jmtool22.png" />
        <span class="f-pt-m">
					恭喜您，店铺创建成功<br />
					<em>公众号已授权绑定成功</em>
				</span>
    </div>
    <div class="u-btn-box1">
        系统　<font id="jumpTo">5</font>　秒后自动跳转进入店铺
    </div>
</div>
<div id="dialoginfo3" class="dialoginfo m-wx-sus">
    <div class="m-sus-box">
        <img class="f-fl" src="${THIRD_URL}/img/pc/jmtool33.png" />
        <span class="f-pt-m">
					很抱歉，店铺无法创建<br />
					<em>公众号授权绑定未成功</em>
				</span>
    </div>
    <div class="u-btn-box1">
        请关闭本页面后，查看并排查失败原因后再次授权
    </div>
</div>
<script src="${basePath}/js/third-js/jquery-1.11.0.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/js/third-js/artDialog/js/dialog-min.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/js/third-js/distpicker/distpicker.data.js?"></script>
<script src="${basePath}/js/third-js/distpicker/distpicker.js"></script>
<script src="${basePath}/js/our-js/dialog_message.js"></script>
<script src="${basePath}/js/busi-js/pc/shop/create_shop.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/js/our-js/jmClient.js" type="text/javascript" charset="utf-8"></script>

<script>
    $(function(){
        $("#u-logo").click(function(){
            $("#othermemu").toggleClass("z-change");
        });
        $(".dialog").click(function() {
            var elem = document.getElementById('dialoginfo');
            dialog({
                title: "弹窗",
                content: elem,

            }).width(500).showModal();

        });
        $(".dialog1").click(function() {
            var elem1 = document.getElementById('dialoginfo1');
            dialog({
                title: "现有角色",
                content: elem1,

            }).width(500).showModal();

        });

        //********************初始化地区控件
        var chooseArea = function(){
        };
        chooseArea.prototype = {
            init:function(){
                this._renderArea();
            },
            _renderArea:function(){
                var $distpicker = $('#distpicker');
                $distpicker.distpicker({
                    province: '${shop.province!'' }',
                    city: '${shop.city!'' }',
                    district: '${shop.district!'' }',
                    autoSelect: false
                });
            },
            _onBindClick:function(){


            },
            _offBindClick:function(){

            }
        };
        var choosearea = new chooseArea();
        choosearea.init();
        //********************初始化地区控件
    })

</script>

</body>

</html>