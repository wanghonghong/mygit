CommonUtils.regNamespace("sales", "valid");
sales.valid=(function(){
    var tabindex=0;
    var opt={
        detailajax: CONTEXT_PATH+ '/brokerage/brokerage_detail/',
        detailtpl:  'tpl/shop/distribution/brokerage/brokerage_detail.html'
    }
    var pageparam=  [{
        url: CONTEXT_PATH+"/brokerage/brokerage_order",
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar1",
        tableBodyObj: "tableBody1",
        searchFrom:"searchform1",
        fristSearch:true,
        template:"/tpl/shop/distribution/brokerage/validitem.html"
    }  ];

    var _init=function () {
        pageparam[0].fristSearch=true;
        bindEvent();
        _search(pageparam[0]);
    }
    var _search=function (pageparam) {
        var data={
            pageSize: pageparam.pageSize,
            curPage: pageparam.curPage,
            status:2     //有效清单
        }
        if(pageparam.searchFrom){
            $("input[type='text'],input[type='number'],select","#"+pageparam.searchFrom ).each(function () {
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
    var bindEvent=function () {
        $("#startDate").datetimepicker({
            timeFormat: 'hh:mm:ss'
        });
        $("#endDate").datetimepicker({
            timeFormat: 'hh:mm:ss'
        });
        $("#platForm").select2({
            theme: "jumi"
        });
        $("#valid .btn-slide").click(function (e) {
            e.preventDefault();
            $(this).siblings(".m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1");
        });
        $("#searchform1 .searchbtn").click(function (e) {
            e.preventDefault();
            pageparam[0].curPage=0;
            _search(pageparam[0]);
        });
    }
    var _detailItem=function (el) {
        var tablecontainer= $(el).parent().parent();
        var url=opt.detailajax+tablecontainer.data().orderInfoId;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                var html = jumi.templateHtml(opt.detailtpl,res.data);
                var dw=      dialog({
                    content: html,
                    title: '订单详情'
                }).width(1180).height(500).showModal();
                $("#detailtable").rowspan([2,3,4]);
            }
        });
    }
    return {
        init:_init,
        detailItem:_detailItem
    };
})();