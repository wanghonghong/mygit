CommonUtils.regNamespace("sales", "brokerageDetail");
sales.brokerageDetail=(function(){
    var tabindex=0;
    var opt={
        containerId: "detailWindow"
    };
    var pageparam=  [{
        url: CONTEXT_PATH+ '/brokerage/brokerage_detail/',
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "itempageToolbar1",
        tableBodyObj: "itemtableBody1",
        status:"0",
        brokerageTotal:0,
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/brokerage_detail_list.html"
    } ,{
        url: CONTEXT_PATH+ '/brokerage/brokerage_detail/',
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "itempageToolbar2",
        tableBodyObj: "itemtableBody2",
        status:"1",
        brokerageTotal:0,
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/brokerage_detail_list.html"
    },{
        url:CONTEXT_PATH+ '/brokerage/brokerage_detail/',
        pageSize: 10,
        curPage: 0,
        brokerageTotal:0,
        pageToolbarObj: "itempageToolbar3",
        tableBodyObj: "itemtableBody3",
        status:"2",
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/brokerage_detail_list.html"
    },{
        url:CONTEXT_PATH+ '/brokerage/brokerage_detail/',
        pageSize: 10,
        curPage: 0,
        brokerageTotal:0,
        pageToolbarObj: "itempageToolbar4",
        tableBodyObj: "itemtableBody4",
        status:"3",
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/brokerage_detail_list.html"
    }];

    var _init=function () {
        tabindex=0;
        pageparam[0].fristSearch=true;
        pageparam[1].fristSearch=true;
        pageparam[2].fristSearch=true;
        pageparam[3].fristSearch=true;
        console.log(pageparam);
        _bindEvent();
        $(".m-tab li:eq("+tabindex+")","#"+opt.containerId).click();
    }
    var _search=function (pageparam) {
       var user_id= $("#tmpuserid").val();
        var data={
            pageSize: pageparam.pageSize,
            curPage: pageparam.curPage,
            status:pageparam.status,
            userId:user_id
        }

        // var data = JSON.stringify(data);
        jumi.pagination("#"+pageparam.pageToolbarObj,pageparam.url,data,function (res,curPage) {
            pageparam.curPage=curPage;
            $("#"+pageparam.tableBodyObj).empty();
            if(res.data.items && res.data.items.length>0){
                var itemhtml = jumi.templateHtml(pageparam.template,res.data);
                $(itemhtml).appendTo("#"+pageparam.tableBodyObj);
                pageparam.brokerageTotal=res.data.ext/100;
                $("#brokerageTotal").html(pageparam.brokerageTotal);
            }else{
                $("<div>",{
                    'class':'m-jm-err'
                }).html(" <img src='"+jumi.config.cssPath+"/css/pc/img/jm-nodb.png' >").appendTo("#"+pageparam.tableBodyObj);
            }
        })
    }
    var  _bindEvent=function () {
        $(".icon-Application","#"+opt.containerId).hover(
            function () {
                $(".m-addressbox","#"+opt.containerId).removeClass("z-hide");
            },
            function () {
                $(".m-addressbox","#"+opt.containerId).addClass("z-hide");
            }
        );

        $(".m-tab li","#"+opt.containerId).click(function () {
            var _this= $(this);
            _this.addClass("z-sel").siblings().removeClass("z-sel");
            var target = _this.attr("data-target");
            var index=_this.index();
            tabindex=index;
            if(pageparam[index].fristSearch){
                pageparam[index].fristSearch=false;
                _search(pageparam[index]);
            }
            $("#brokerageTotal").html(pageparam[index].brokerageTotal);
            $("#"+target).removeClass("z-hide").siblings(".panel-hid").addClass("z-hide");

        })
    }

    return {
        init:_init
    }
})();