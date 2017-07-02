CommonUtils.regNamespace('menuDialog');
menuDialog = (function(){
    "use strict";
    var _menu;
    var _getMenulink = function(options,callback){
       try{
           if($("#tag-details-content").find('ul').length>0){
               return;
           }
           var url = CONTEXT_PATH+'/menu/setMenuLink';
           var json_data = JSON.parse(localStorage.getItem("json"));
           var saveTime = localStorage.getItem("saveTime");
           if(!json_data||new Date().getTime()-saveTime>=10*60*1000){
               $.ajaxJsonGet(url,'',{
                   done:function(res) {
                       if(res.code==0){
                           var json = JSON.stringify(res.data);
                           var json = localStorage.setItem("json",json);
                           var time = localStorage.setItem("saveTime",new Date().getTime());
                           var json_data = JSON.parse(localStorage.getItem("json"));
                           menuDataRender(options,json_data,callback);
                       }else{
                           alert(res.msg);
                       }
                   }
               })
           }
           else{
               menuDataRender(options,json_data,callback);
           }
       }catch(e){
           console.error(e);
       }

    };
    var Menu = function(options){
        this.memudialog = dialog({
            id:'show-dialog',
            width: 702,
            title: '菜单链接设置',
            modal:options.modal||false,
            content: document.getElementById("setMenuLink"),
            button:[{
                value:'关闭'
            }]
        }).show();
        this.options = options;
    };
    Menu.prototype.getUrl = function(callback){
        var that = this;
        var sorts = $("#tag-details-content").find('.tag-details > .sort');
        $(sorts).unbind('click');
        $(sorts).each(function(i,li){
            $(li).bind('click',function(){
                var link = _getProperties($(this));
                that.memudialog.close().remove();
                if(typeof callback ==='function'){
                    callback(link);
                }
                $(this).unbind('click');
            });
        });
       var queryLinks =  $("#tag-details-content").find('.tag-details > .link');
        $(queryLinks).unbind('click');
        $(queryLinks).each(function(i,li){
            $(li).bind('click',function(){
                var options = _getProperties($(this));
                options.shop_id= that.options.shop_id;
                menuDialog.helper.getUrlValueRequest(options,function(link){
                    $("#text_pageToolbar").css('display','none');
                    $("#products").html('');
                    that.memudialog.close().remove();
                    if(typeof callback==='function'){
                        callback(link);
                    }
                });
            });
        });
        var eventBtns = $("#tag-details-content").find('.tag-details > .event');
        $(eventBtns).unbind('click');
        $(eventBtns).bind('click',function(){

            var link = _getProperties($(this));
            that.memudialog.close().remove();
            if(typeof callback ==='function'){
                callback(link);
            }
        });
        var custom = $('.custom').bind('click',function(){
            $(this).unbind('click');
            var url =$('.custom-link').val();
            var reg = /^((https?|ftp|news):\/\/)?([a-z]([a-z0-9\-]*[\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\/[a-z0-9_\-\.~]+)*(\/([a-z0-9_\-\.]*)(\?[a-z0-9+_\-\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$/;
            if(!reg.test(url)){
                return ;
            }
            var link ={
                type:'custom',
                link_url:url,
                link_type:'4'
            };
            that.memudialog.close().remove();
            if(typeof callback==='function'){
                callback(link);
            }
        });
    };
    var _getProperties = function(obj){
        var attributes = ['parent_id','link_id','link_url','link_name','link_type','type','link_key','shop_id','data-name'];
        var link = {};
        for(var i in attributes){
            var attr = $(obj).attr(attributes[i]);
            link[attributes[i]] = attr;
        }
        return link;
    }
    Menu.initPage = function(options,callback){
      //  console.log($("#link_content").html().trim().length);
        if($("#link_content")[0].hasChildNodes()&&$("#link_content").html().trim().length>0){
            if(typeof callback==='function'){
                var menu = new Menu(options);
                _menu = menu;
                if(typeof callback==='function'){
                    callback(menu);
                }
            }
        }else{
            console.log('menu dialog init.......');
            jumi.template('common/menu/setMenulink',function(tpl){
                $("#link_content").empty();
                $('#setMenuLink').remove();
                $("#link_content").append(tpl);
                _getMenulink(options,function(){
                    if(typeof callback==='function'){
                        var menu = new Menu(options);
                        _menu = menu;
                        if(typeof callback==='function'){
                            callback(menu);
                        }
                    }
                });
            });
        }
    };

    var  menuDataRender = function(options,json_data,callback){
        var array = _.where(json_data,{'parent_id':0});
        console.log('links',json_data,array);
        var data = {
            modules:array,
            links:json_data,
            options:options
        };
        jumi.template('common/menu/link_content',data,function(tpl){
            try{
                $("#tag-details-content").append(tpl);
                $('#btnClose').click(function(){
                    "use strict";
                    dialog.get('show-dialog').close();
                })
                $('#setMenuLink').find('.sort').on('mouseover',function(){
                    $('.sort').removeClass('sort-active');
                    $(this).addClass('sort-active');
                });
                $('#setMenuLink').find('.sort').on('mouseout',function(){
                    $('.sort').removeClass('sort-active');
                });
                if(typeof callback ==='function'){
                    callback();
                }
            }catch(e){
                console.error(e);
            }
        });
    };
    return {
        Menu:Menu,
        getLink:_getProperties,
    }
}());