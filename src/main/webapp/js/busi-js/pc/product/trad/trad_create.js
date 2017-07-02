
/**
 * 交易活动创建 modify by pray
 */
CommonUtils.regNamespace("trad","create");
trad.create=(function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+"/product/trad",//创建活动增删改查
        url2:CONTEXT_PATH+"/product/trad/trad_products"//活动商品列表查询
    }
    //初始化入口
    var _init = function(){
        _bind();
        _getAction();
        _validateset();
    }
    /**
     * 验证信息
     */
    var _validateset = function(){

        $('#trad_set_form').validate({
            rules: {
                action_name:{
                    required: true
                },
                product_price:{
                    required: true,
                    isFloatGtZero:true,
                    isPlusTwo:true
                }
            },
            messages:{
                action_name:{
                    required: "请输入活动名称"
                },
                product_price:{
                    required: "请输入商品拼团价",
                    isFloatGtZero:'请输入有效金额',
                    isPlusTwo:"金额必须为小数或整数,小数点后不超过两位"
                }
            }
        })
    }


    /**
     * 绑定事件
     * @private
     */

    var _bind = function () {
        $('#button_save').click(function () {
            _doSubmit();
        })
        $('#btn_return').click(function () {
            $('#create_sus').hide();
            $('#create_trad').show();
        })
        $('input[name="hdxz"]').click(function () {
            var value = $(this).val();
            _getAction();
        })

        $('#chooseProduct').click(function () {
            var pid = $('input[name="chooseProductBoxName"]').val();
            var goodsDialog = new GoodsDialog({
                context_path: CONTEXT_PATH,
                isSingle:true
            }, function (good) {
                var tpl = '<p data-goodPrice="'+good.price/100+'" data-pid="'+good.pid+'" title="'+good.name+'" style="max-height: 36px; line-height: 34px;">'+good.name+'</p>'
                $('#chooseProductBox').html(tpl);
                $('#xg_preview').attr('src',good.pic_square);

            })
            goodsDialog.render();
        })

        $('input[name="weiz"]').click(function () {
            var self = $(this);
            var position = self.data('positon');
            var positionArray = position.split(',');
            $("#img_position").removeAttr('style');
            _.map(positionArray,function (k,v) {
                $("#img_position").css(positionArray[v],0+ "px");
            })

        })

    }
    /**
     * 获取活动
     * @private
     */
    var _getAction = function () {
        var type = $('input[name="hdxz"]:checked').val();
        $.ajaxJsonGet(ajaxUrl.url1,{
            "done":function (res) {
                if(res.code===0){
                    _getImgType(res.data,type);
                }
            },
            "fail":function () {

            }
        });
    }


    /**
     * 活动分类
     */
    var _getImgType = function (json,type) {
        var t = Number(type),isHasCustomType;
        var jsonData = _.where(json,{type:t});
        var imgType =  _.where(json,{imgType:1});
        if(imgType.length>0){
            isHasCustomType = 1;
        }else{
            isHasCustomType = 0;
        }
        var data = {
            items:jsonData,
            isHasCustomType:isHasCustomType
        }
        jumi.template('product/trad/trad_img_type',data,function (tpl) {
            $('#trad_img_type').html(tpl);
            $('input[name="yangs"]').click(function () {
                var self = $(this);
                var url=self.data('imgurl');
                $('#img_position').attr('src',url);
            })
            $('#diy_img').click(function () {
                var d = new Dialog({
                    context_path:CONTEXT_PATH, //请求路径,  必填
                    resType:1 ,//图片1，视频2，语音3 必填
                    callback:function(url){
                        $('#diy_img').attr('src',url);
                    }
                });
                d.render();
            })
        })
    }
    /**
     * 创建活动
     * @private
     */

    var _doSubmit = function () {
        var tradActivityCo = {};
        var price = $('input[name="product_price"]').val();//拼团价格
        var productPrice = $('#chooseProductBox').find('p').data('goodprice');
        tradActivityCo.type = $('input[name="hdxz"]:checked').val();
        tradActivityCo.name = $('input[name="action_name"]').val();
        tradActivityCo.indianaType = $('input[name="activity_space"]:checked').val();
        tradActivityCo.sign = $('input[name="yangs"]:checked').val();
        if(tradActivityCo.sign==='99'){
            var url = $('#diy_img').attr('src');
            var purl = jumi.picParse(url,0);
            tradActivityCo.imageUrl = purl
        }
        tradActivityCo.groupType = $('#trad_type').val();
        tradActivityCo.pid = $('#chooseProductBox').find('p').data('pid');
        tradActivityCo.price = parseInt(price,10)*100;
        tradActivityCo.position = $('input[name="weiz"]:checked').val();
        var flag = $('#trad_set_form').valid();
        if(tradActivityCo.type==='2'&&price>productPrice){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'拼团价格不能大于商品价格',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        if(!tradActivityCo.pid){
            var dm = new dialogMessage({
                type:1,
                fixed:true,
                msg:'请选择活动商品',
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return;
        }
        if(!flag){
            return;
        }
        var jsonData = JSON.stringify(tradActivityCo);
        $.ajaxJson(ajaxUrl.url1,jsonData,{
            "done":function (res) {
                if(res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'新增活动成功',
                        isAutoDisplay:true,
                        time:3000
                    });
                    dm.render();
                    $('#create_trad').hide();
                    $('#create_sus').show();
                }
            }
        })

    }
    return {
        init:_init
    };
})();
