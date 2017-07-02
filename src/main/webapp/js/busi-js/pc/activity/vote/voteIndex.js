/**
 * Created by zx on 2017/4/5.
 */
CommonUtils.regNamespace("vote","index");

vote.index=(function () {
    var tabindex=0;

    var pageparam=  [{
        url: CONTEXT_PATH+"/vote/items",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar1",
        tableBodyObj: "tableBody1",
        searchFrom:"searchform1",
        fristSearch:true,
        template:"/tpl/activity/vote/voteItemList.html"
    } ,{
        url: CONTEXT_PATH+"/vote/themes",//主题列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar2",
        tableBodyObj: "tableBody2",
        searchFrom:"searchform2",
        fristSearch:true,
        template:"/tpl/activity/vote/voteThemeList.html"
    }];

    var _init=function () {
        tabindex=0;
        pageparam[0].fristSearch=true;
        pageparam[1].fristSearch=true;
        _bindEvent();
        // _search(pageparam[0]);
        _queryItemList();
    }
    var _search=function (pageparam) {
        var data={
            pageSize: pageparam.pageSize,
            curPage: pageparam.curPage
        }
        if(pageparam.searchFrom){
            $("input[type='text'],input[type='hidden'],select","#"+pageparam.searchFrom ).each(function () {
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
            var itemhtml = jumi.templateHtml(pageparam.template,res.data);
            $(itemhtml).appendTo("#"+pageparam.tableBodyObj);
        })
    }
    var _queryItemList=function () {
        _search(pageparam[0]);
    }
    var _queryThemeList=function () {
        _search(pageparam[1]);
    }
    var _bindEvent=function () {
        $("#voteItemBox [name$='Time']").datetimepicker({ timeFormat:'hh:mm:ss'  });
        $("#voteThemeBox [name$='Time']").datetimepicker({ timeFormat:'hh:mm:ss'  });

        $("#voteItemBox select").select2({
            theme: "jumi"
        });
        $("#voteThemeBox select").select2({
            theme: "jumi"
        });
        $(".btn-slide1").click(function(){
            $("#searchform1").slideToggle("fast");
            $(this).toggleClass("btn-slide1"); return false;
        });
        $(".btn-slide2").click(function(){
            $("#searchform2").slideToggle("fast");
            $(this).toggleClass("btn-slide3"); return false;
        });
        $("#themeSearchBtn").click(function () {
            // _search(pageparam[1]);
            _queryThemeList();
        })
        $("#itemSearchBtn").click(function () {
            _queryItemList();
        })
        $("#vote .m-tab li").click(function () {
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
    }
    return {
        init:_init,
        queryItemList:_queryItemList,
        queryThemeList:_queryThemeList
    };
})();
