/**
 * Created by ray on 16/11/17.
 */
CommonUtils.regNamespace("awardpoint", "exchange");
awardpoint.exchange = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/good/productList/0',//获取在售商品
        url2:CONTEXT_PATH+'/integral/product',
        url3:CONTEXT_PATH+'/integral/products',
        url4:CONTEXT_PATH+'/integral/product/list'
    };
    var _init = function(){
        _queryAwardpointList();
        _bind();
    }

    //商品换购列表
    var _queryAwardpointList = function(){
        var url = ajaxUrl.url3;
        var integralProductQo = {
            pageSize:10
        };
        jumi.pagination('#pagePointToolbar',url,integralProductQo,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('awardpoint/awardpoint_exchange_list',data,function(tpl){
                    $('#exchange_list').html(tpl);
                })
            }
        })
    }
    var _refreshPage = function(){
        var curPage = $('#pagePointToolbar_page').val();
        var integralProductQo = {
            pageSize:10,
            curPage:curPage
        };
        $.ajaxJson(ajaxUrl.url3,integralProductQo,{
            "done":function(res){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    };
                    jumi.template('awardpoint/awardpoint_exchange_list',data,function(html){
                        $('#exchange_list').html(html);
                    })
                }
            },
            "fail":function(){

            }
        })
    }
    var _validateset = function(){
        $('#awardpoint_set_form').validate({
            rules: {
                award_integral:{
                    required: true,
                    digits:true,
                    isIntGtZero:true
                }
            },
            messages:{
                award_integral:{
                    required: "请输入积分数值",
                    isIntGtZero:'积分数值必须是一个大于零的整数',
                    digits:'积分数值必须是一个大于零的整数'
                }
            }
        })
    }
    var _bind = function(){
        $('#awardpoint_content').on('click','input[name="allChose"]',function(){
            var flag = $(this).is(':checked');
            if(flag){
                $('input[name="checkall"]').prop('checked',true);
            }else{
                $('input[name="checkall"]').prop('checked',false);
            }
        })
        $('#awardpoint_content').on('click','#awardpoint_all',function(){
            var integralProductCos = [];
            $('input[name="checkall"]:checked').each(function(){
                var _object = {};
                var id = $(this).data('id');
                var pid = $(this).val();
                _object.id = id;
                _object.pid = pid;
                integralProductCos.push(_object);
            })
            if(integralProductCos.length>0){
                $.ajaxJson(ajaxUrl.url4,integralProductCos,{
                    "done":function (res) {
                        if(res.code===0){
                            _refreshPage();
                        }
                    }
                })
            }else{
                var dm = new dialogMessage({
                    type:2,
                    fixed:true,
                    msg:'还未勾选商品',
                    isAutoDisplay:true,
                    time:3000
                });
                dm.render();
            }
        })
        //商品换购删除
        $('#awardpoint_content').on('click','div[id^="awardpoint_del_"]',function(){
            var args = {};
            var id = $(this).data('id');
            args.fn1 = function(){
                var url = ajaxUrl.url2+'/'+id;
                $.ajaxJsonDel(url,{
                    "done":function (res) {
                        if(res.code===0){
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'清空商品换购成功',
                                isAutoDisplay:true,
                                time:3000
                            });
                            dm.render();
                            _refreshPage();
                        }
                    }
                });

            };
            //关闭的时候初始化方法
            args.fn2 = function(){

            };
            jumi.dialogSure('确定清空该商品换购吗?',args);

        })
        //商品换购编辑按钮
        $('#awardpoint_content').on('click','div[id^="awardpoint_edit_"]',function(){
            var id = $(this).data('id'),type;
            var pid = $(this).data('pid');
            var price = $(this).data('price');
            var integral = $(this).data('integral');
            var state = $(this).data('type');
            var data = {};
            data.price = price;
            data.id = id;
            data.pid = pid;
            data.type = state;
            data.integral = integral;
            if(id){
                type=1;//如果id存在表示设置过积分
            }
            jumi.template('awardpoint/awardpoint_set?type=1',data,function(tpl){
                var d = dialog({
                    title: '商品积分设置',
                    content:tpl,
                    id:'awardpoint_set_dialog',
                    width:500,
                    height:190,
                    onshow:function () {
                        _validateset();
                        $('#awardpoint_save').click(function(){
                            var form = $('#awardpoint_set_form');
                            var type = $('input[name="award_buy-r"]:checked').val();
                            var integralProductCo = {};
                            if(type==='1'){
                                if(form.valid()){
                                    integralProductCo.integral = $('input[name="award_integral"]').val();
                                }else{
                                    return;
                                }
                            }
                            var id = $('#award_set').val();
                            integralProductCo.pid = $(this).data('pid');
                            integralProductCo.type = type;
                            if(id){
                                integralProductCo.id = id;
                            }
                            var jsonData = JSON.stringify(integralProductCo);
                            $.ajaxJson(ajaxUrl.url2,jsonData,{
                                "done":function(res){
                                    if(res.code===0){
                                        _refreshPage();
                                        dialog.get('awardpoint_set_dialog').close().remove();
                                    }
                                },
                                "fail":function(){
                                    var dm = new dialogMessage({
                                        type:2,
                                        fixed:true,
                                        msg:'设置失败',
                                        isAutoDisplay:true,
                                        time:3000
                                    });
                                    dm.render();
                                }
                            })
                        })
                    }

                });
                d.showModal();
            })

        })
    }
    return {
        init: _init
    };
})();