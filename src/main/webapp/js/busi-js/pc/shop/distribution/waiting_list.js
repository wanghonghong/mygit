CommonUtils.regNamespace("sales", "waiting");

sales.waiting=(function(){
    var tabindex=0;
    var opt={
            brokerageRedAjax:CONTEXT_PATH+"/brokerage/red/hand_send/",
        brokeragekitAjax:CONTEXT_PATH+"/brokerage/red/user_kit/",
       putoutTpl:"/tpl/shop/distribution/brokerage/putout_item.html",
        waitingTpl:"/tpl/shop/distribution/brokerage/waiting_item.html",
        detailWindowTpl:"/tpl/shop/distribution/brokerage/brokerage_detail_window.html"
    }
    var pageparam=  [{
            url: CONTEXT_PATH+"/brokerage/wx_account",//商家发放
            pageSize: 10,
            curPage: 0,
            pageToolbarObj: "pageToolbar2",
            tableBodyObj: "tableBody2",
            searchFrom:"searchform2",
             fristSearch:true,
            template:"/tpl/shop/distribution/brokerage/putout.html"
        } ,{
        url: CONTEXT_PATH+"/brokerage/wx_account_kit",//客户提现
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar1",
        tableBodyObj: "tableBody1",
        searchFrom:"searchform1",
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/withdrawcash.html"
    },{
        url: CONTEXT_PATH+"/brokerage/wx_account",//其他客户
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar3",
        tableBodyObj: "tableBody3",
        searchFrom:"searchform3",
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/otherClient.html"
    }];

    var _init=function () {
        tabindex=0;
        pageparam[0].fristSearch=true;
        pageparam[1].fristSearch=true;
        pageparam[2].fristSearch=true;
        _bindEvent();
        $("#waiting .m-tab li:eq("+tabindex+")").click();
    }
    var _search=function (pageparam) {
        var data={
            pageSize: pageparam.pageSize,
            curPage: pageparam.curPage
        }
        if(pageparam.searchFrom){
            $("input[type='text'],select","#"+pageparam.searchFrom ).each(function () {
               if( $(this).attr("name")){
                if($(this).val()){
                    data[$(this).attr("name")]=$(this).val();
                }
                }
            })
        }

        // var data = JSON.stringify(data);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,data,function (res,curPage) {
               pageparam.curPage=curPage;
            $("#"+pageparam.tableBodyObj).empty();
            if(res.data.items && res.data.items.length>0){
               for(var i=0;i<res.data.items.length;i++){
                   if(tabindex==2){
                       res.data.items[i].other=$("#otherClientplatother").val();
                   }
                   var itemhtml = jumi.templateHtml(pageparam.template,res.data.items[i]);
                   var item = $(itemhtml).data(res.data.items[i]);
                   $(item).appendTo("#"+pageparam.tableBodyObj);
               }
            }else{
                $("<div>",{
                    'class':'m-jm-err'
                }).html(" <img src='"+jumi.config.cssPath+"/css/pc/img/jm-nodb.png' >").appendTo("#"+pageparam.tableBodyObj);
            }
        })
    }
   var  _bindEvent=function () {
        $("#startDate").datetimepicker({ timeFormat:'hh:mm:ss'  });
        $("#endDate").datetimepicker({ timeFormat:'hh:mm:ss'  });
       $("#withdrawcashplatForm").select2({
           theme: "jumi"
       });
       $("#putoutplatForm").select2({
           theme: "jumi"
       });
       $("#otherClientplatForm").select2({
           theme: "jumi"
       });
       $("#otherClientplatother").select2({
           theme: "jumi"
       });
        $("#waiting .m-tab li").click(function () {
            var _this= $(this);
            _this.addClass("z-sel").siblings().removeClass("z-sel");
            var target = _this.attr("data-target");
            var index=_this.index();
            tabindex=index;
            if(pageparam[index].fristSearch){
                pageparam[index].fristSearch=false;
                _search(pageparam[index]);
            }
            $("#"+target).removeClass("z-hide").siblings(".panel-hidden").addClass("z-hide");

        })
        $("#waiting .btn-slide").click(function (e) {
            e.preventDefault();
            $(this).siblings(".m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1");
        });
        $("#waiting .panel-hidden .searchbtn").click(function (e) {
            e.preventDefault();
            var index=$(this).closest(".panel-hidden").index();
            pageparam[index].curPage=0;
            _search(pageparam[index]);
        });
    }
    var _auditOrder=function (el) {
       var tablecontainer= $(el).parent().parent();
        var data=tablecontainer.data();
        var html = jumi.templateHtml(opt.putoutTpl,data);
          dialog({
            content: html,
            title: '发放操作',
            okValue: '确定发放',
            ok: function() {
                if(data.balance<100){
                    var dm = new dialogMessage({
                        type:2,
                        fixed:true,
                        msg:"/(ㄒoㄒ)/~~ 金额小于1元不支持发送"
                    });
                    dm.render();
                }else{
                    var url=opt.brokerageRedAjax+data.userId;
                    $.ajaxJsonGet(url,null,{
                        "done":function (res) {
                            console.log(res);
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                            });
                            dm.render();
                            _search(pageparam[tabindex]);
                        }
                    })
                }
            },
            cancelValue:'取消',
            cancel:function () { }
        }).width(500).height(140).showModal();

    }
    var _lookup=function (el) {
        var tablecontainer= $(el).parent().parent();
        var data=tablecontainer.data();
        var html = jumi.templateHtml(opt.detailWindowTpl,data);
        dialog({
            content: html,
            title: '佣金列表'
        }).width(845).height(500).showModal();
        sales.brokerageDetail.init();
    }
    var _review=function (el) {
        var tablecontainer= $(el).parent().parent();
        var data=tablecontainer.data();
        var html = jumi.templateHtml(opt.waitingTpl,data);
        dialog({
            content: html,
            title: '操作提现',
            okValue: '确定',
            ok: function() {
                    var url=opt.brokeragekitAjax+data.id;
                    $.ajaxJsonGet(url,null,{
                        "done":function (res) {
                            console.log(res);
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:res.data.msg,
                            });
                            dm.render();
                            _search(pageparam[tabindex]);
                        }
                    })

            },
            cancelValue:'取消',
            cancel:function () { }
        }).width(500).height(140).showModal();

    }
    return {
        init:_init,
        auditOrder:_auditOrder,
        lookup:_lookup,
        review:_review
    };
})();