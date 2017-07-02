<!--主模块-->
<section>
    <div class="g-addshopbox f-mt-xl">
        <div class="g-addshop f-pt-s">
            <div class="col-xs-12 f-tc f-mb-xl">
                <div class="u-wx-point f-mb-l">
                    <p>绑定微信公众号，把店铺和微信打通</p>
                    <p>绑定后即可在这里管理您的公众号，聚米为谷提供比微信官方更强大的功能</p>
                </div>
                <a onclick="goAuthpage();">
					<div class="u-btn-weixin">
						<i class="iconfont icon-wechat"></i>再次授权<i class="iconfont icon-ensure"></i>
					</div>
				</a>
            </div>
            <dl class="col-xs-11 u-wx-explain f-mb-l">
                <dt>温馨说明：</dt>
                <dd>1、本次授权绑定未成功，可能因各种原因产生绑定失败，请自行排查！查看绑定失败原因请点击　　<a href="#">查看失败原因</a></dd>
                <dd>2、如各种原因所描述问题均不存在，可能因系统缓冲原因产生绑定失败，请点击上方再次授权！</dd>
                <dd>3、公众号授权绑定成功，您所新建的店铺才能启用，您方可进入聚米为谷为你打造的移动电商系统！</dd>
            </dl>
        </div>
    </div>
</section>
<!--/主模块-->
<script>
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

</script>