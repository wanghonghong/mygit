CommonUtils.regNamespace('menuDialog','helper');
menuDialog.helper = (function(){
    "use strict";
    var  _getUrlValueRequest = function(options,callback){
        $('#text_pageToolbar').hide();
        //memudialog.height(800);
        console.log(options);
        if(options.link_name=="商品分类"){
            var newurl = "/app/groupList";
            options.newurl=newurl;
            _goods_classify_bar(options,callback);
        }else if(options.link_name=="具体商品"){
            var newurl = "/product";
            options.newurl = newurl;
            _goods_definite_bar(options,callback);
        }
        else if(options.link_name=="官方图文列表"){
            var newurl = "/imageText";
            options.newurl = newurl;
            _itemtext_page_bar(options,callback);
        }
        else if(options.link_name=="乐享图文列表"){
            var newurl = "/imageText";
            options.newurl = newurl;
            _enjoytext_page_bar(options,callback);
        }else if(options.link_name=="官方图文分类"){
            var newurl = "/shop/image_text_type";
            options.newurl = newurl;
            _itemtext_page_bar1(options,callback);
        }
        else if(options.link_name=="乐享图文分类"){
            var newurl = "/shop/image_text_type";
            options.newurl=newurl;
            _enjoytext_page_bar1(options,callback);
        }else if(options.link_name=="培训通知列表"){
            var newurl = "/imageText";
            options.newurl =newurl;
            _enjoytext_page_bar2(options,callback);
        }else if(options.link_name=="培训通知分类"){
            var newurl = "/shop/image_text_type";
            options.newurl =newurl;
            _itemtext_page_bar2(options,callback);
        }else if(options.link_name==='投票活动'){
            var newurl = "/activity/vote_theme/";
            options.newurl = newurl;
            _voteList(options,callback);
        }else if(options.link_name==='活动报名'){
            options.newurl = "/enrolment/";
            _enrolmentList(options,callback);
        }
    };
    var _goods_classify_bar=function(options,callback){
        var curl=CONTEXT_PATH+'/product/group/groups';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            console.log(res);
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                console.log(data);
                jumi.template('common/menu/goodsclassify_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                });
            }
        });
    }
    var _goods_definite_bar=function(options,callback){
        var curl=CONTEXT_PATH+'/good/productList/0';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ulprice=(parseFloat(data.items[i].price)/100).toFixed(2);
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                jumi.template('common/menu/definitegoods_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        })
    };


////项目图文分页start
    var _itemtext_page_bar=function(options,callback){
        var curl=CONTEXT_PATH + '/image_text/findAll/1';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        tmpdata.flag="Y"; //上架
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                // console.log(data);
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                console.log(data);
                jumi.template('common/menu/itemtext_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        })
    }
////项目图文分页end
////乐享图文分页start
    var _enjoytext_page_bar=function(options,callback){
        var curl=CONTEXT_PATH + '/image_text/findAll/2';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        tmpdata.flag="Y";//上架
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            console.log(res);
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                console.log(data);
                jumi.template('common/menu/enjoytext_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    console.log(111);
                    _bandEvent(callback);
                })
            }
        })
    };
    var _enjoytext_page_bar1=function(options,callback){
        var curl=CONTEXT_PATH + '/image_text_type/list/type_id/2';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        tmpdata.flag="Y";//上架
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){

            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.imageTextTypes
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                // console.log(data);
                for(var i=0;i<data.items.items.length;i++){
                    data.items.items[i].ultype=options.link_type;
                    data.items.items[i].ulurlname=options.link_name;
                    data.items.items[i].ulnewurl=options.newurl;
                    data.items.items[i].ulkey=options.link_key;
                    data.items.items[i].ulshopId=options.shop_id;
                    data.items.items[i].parent_id = options.parent_id;
                    data.items.items[i].type = options.type;
                }
                jumi.template('common/menu/enjoytext_list1',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        })
    };
    ////微信图文分页start
    var _itemtext_page_bar1=function(options,callback){
        var curl=CONTEXT_PATH + '/content/content_list';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        tmpdata.sendType="mpnews",
            $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                // console.log(data);
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }

                jumi.template('common/menu/itemtext_list_wechat',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        })
    };
////微信图文分页end
    var _bandEvent = function(callback){
        var labels =  $("#products").find('.m-jm-table > .table-body > .table-container > li > div >label');
        $(labels).each(function(i,label){
            $(this).bind('click',function(){
                var link = menuDialog.getLink($(this));
                if(typeof callback ==='function'){
                    callback(link);
                }
            });
        });
    };

    ////培训通知分类分页start
    var _itemtext_page_bar2=function(options,callback){
        var curl=CONTEXT_PATH + '/image_text_type/list/type_id/3';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        tmpdata.flag="Y";//上架
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.imageTextTypes
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                // console.log(data);
                for(var i=0;i<data.items.items.length;i++){
                    data.items.items[i].ultype=options.link_type;
                    data.items.items[i].ulurlname=options.link_name;
                    data.items.items[i].ulnewurl=options.newurl;
                    data.items.items[i].ulkey=options.link_key;
                    data.items.items[i].ulshopId=options.shop_id;
                    data.items.items[i].parent_id = options.parent_id;
                    data.items.items[i].type = options.type;
                }
                jumi.template('common/menu/itemtext_list1',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        })
    };

////培训通知列表分页start
    var _enjoytext_page_bar2=function(options,callback){
        var curl=CONTEXT_PATH + '/image_text/findAll/3';//ajax请求的链接
        var tmpdata={};
        tmpdata.pageSize=5;
        tmpdata.curPage=0;
        tmpdata.flag="Y";//上架
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                jumi.template('common/menu/enjoytext_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        })
    };

    var _voteList = function(options,callback){
        var url = CONTEXT_PATH+"/vote/themes";
        var tmplate={};
        tmplate.pageSize=5;
        tmplate.curPage=0;
        tmplate.voteType=0;
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',url,tmplate,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                jumi.template('common/menu/vote_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        });
    };

    var _enrolmentList = function (options,callback) {
        var url = CONTEXT_PATH+"/enrolmentActivity/list";
        var tmplate={};
        tmplate.pageSize=5;
        tmplate.curPage=0;
        tmplate.status = 1;
        tmplate.voteType=0;
        $('#text_pageToolbar').show();
        $("#page").children().remove();
        jumi.pagination('#text_pageToolbar',url,tmplate,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                if(curPage===0){
                    data.isFirstPage = 1;
                }else{
                    data.isFirstPage = 0;
                }
                for(var i=0;i<data.items.length;i++){
                    data.items[i].ultype=options.link_type;
                    data.items[i].ulurlname=options.link_name;
                    data.items[i].ulnewurl= options.newurl;
                    data.items[i].ulkey= options.link_key;
                    data.items[i].ulshopId=options.shop_id;
                    data.items[i].parent_id = options.parent_id;
                    data.items[i].type = options.type;
                }
                jumi.template('common/menu/enrolment_list',data,function(tpl){
                    $('#products').empty();
                    $('#products').html(tpl);
                    _bandEvent(callback);
                })
            }
        });
    };

    return {
       getUrlValueRequest:_getUrlValueRequest
    }
}());