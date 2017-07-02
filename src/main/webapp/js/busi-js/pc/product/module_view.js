
/**
 * Created by zx on 2017/2/28.
 */
CommonUtils.regNamespace("module", "view");

module.view=(function(){
    var GOODS_MANAGEMENT=0;  //商品管理
    var MALL_BUILDING=1;  //商城搭建
    var OFFICIAL_IMAGE=2;  //官方图文
    var VOTE_THEME=3;  //投票主题
    var opt={
        viewPath:STATIC_URL+'/tpl/product/detail/view/',
        pagetype:GOODS_MANAGEMENT,
    }
    var  _getid= function(){
        var id =  new Date().getTime();
        return id;
    }
    var alertinfo = function (msg) {
        var dm = new dialogMessage({
            type:3,
            msg:msg,
            isAutoDisplay:true,
            time:1500
        });
        dm.render();
    }
//插入新模块
    var insertPageModular=function (data, pageEl) {
        var sign=true;
        if(data.limitNumber){
            var tmplimit=0;
            $(".app-field",pageEl).each(function () {
                var tdata= $(this).data();
                if(tdata.type==data.type){
                    tmplimit++;
                    if(tmplimit>=data.limitNumber){
                        sign=false;
                        alertinfo("该组件只能有"+data.limitNumber+"个");
                    }
                }
            })
        }
        if(sign){
            var  appid="appid"+ _getid();
            var html = jumi.templateHtml('app-field.html',data,opt.viewPath);
            var appfield=$(html).data(data);
            if (data.fixedTop) {
                if (data.type == "title") {
                    appfield.prependTo($(".app-config", pageEl));
                } else {
                    appfield.appendTo($(".app-config", pageEl));
                }
            } else if (data.fixedBottom) {
                appfield.prependTo($(".app-bottom", pageEl));
            } else {
                appfield.appendTo($(".js-fields-region", pageEl));
            }
            updataPageModular(appfield);
            appfield.find(".control-mask").click();
        }
    };
    var updataPageModular=function (element) {
        var data = $(element).data();
        correctData(data);
        var controlgroup = $(element).find(".control-group");
        controlgroup.children().remove();
        //配置模版路径 富文本等信息统一先不进行转码
        var html = jumi.templateHtml(data.type+'.html',data,opt.viewPath,{ autoescape: false });
        $(html).appendTo(controlgroup);
        if(data.type=='magicSquare'){
            //变态的魔方需要另外脚本附加处理
            data.subEntry=_.compact(data.subEntry);
            _.each(data.subEntry,function (item,k) {
                for(var i=0;i<item.col;i++){
                    for(var j=0;j<item.row;j++){
                        if(i==0&&j==0){
                        }else{
                            $(".magic-view-table tr td[data-x='"+(item.x+i)+"'][data-y='"+(item.y+j)+"']",element).remove();
                        }
                    }
                }
                var itemhtml="<sppan>"+item.row+" * "+item.col+"</span>";
                var colorclass=" f-color-"+(k+1);
                if(item.imageUrl){
                    itemhtml="<img src='"+item.imageUrl+"'/>";
                    colorclass="";
                }
                delete item.selectdata;
                $(" .magic-view-table tr td[data-x='"+item.x+"'][data-y='"+item.y+"']",element).attr({
                    "rowSpan":item.row,
                    "colSpan":item.col
                }).addClass("f-cols-"+item.col+" f-rows-"+item.row+" no-empty "+colorclass)
                    .removeClass("empty").removeAttr("data-x").removeAttr("data-y").html(itemhtml);
            })
        }else if(data.type=='richtext'||data.type=='richtextlimit'){
        //处理呼吸点效果  。。。。
            $('.hotSpotImg',element).each(function () {
                var _this=this;
                var hotspotdataStr=$(_this).attr("hotspotdata");
                var hotspotdata=JSON.parse(hotspotdataStr);
                if(hotspotdata.spotlist.length>0){
                    var pt=$("<div>",{
                        class:"imageItem"
                    })
                    $(_this).wrapAll(pt);
                    var customHotSpot=$("<div>",{
                        "class":"custom-hotSpot"
                    }).insertAfter(_this);
                    $(hotspotdata.spotlist).each(function (i) {
                        var item= hotspotdata.spotlist[i];
                        $("<div>",{
                           "class":"hotSpot"
                        }).css({
                             "top":item.y+"%",
                            "left":item.x+"%"
                        }).appendTo(customHotSpot);
                    })
                }

            })
        }
    }

    //修正数据
    var correctData=function (data) {
        data.randomNum=_getid();

        if(data.type==="title"){
            data.pageTitle=$("#goodsname").val()||$("#shopname").val()||data.pageTitle||"商品标题";
        }else if(data.type==="weixintop"){
            data.timeTxt = data.timeTxt || new Date().Format('yyyy-MM-dd');
            data.authorTxt = data.authorTxt ||"匿名作者"|| $("#shopName").val();
        }else if(data.type=="goods"){
            if(!$.isEmptyObject(data.subEntry)){
                data.items=data.subEntry
                $.each(data.items,function (i) {
                    data.items[i].pid= data.items[i].pid ||  data.items[i].goodsId;
                    data.items[i].name= data.items[i].name ||  data.items[i].goodsName;
                    delete   data.items[i].goodsId;
                    delete   data.items[i].goodsName;
                })
                delete  data.subEntry;
            }
        }
    }

    return{
        insertPageModular:insertPageModular,
        updataPageModular:updataPageModular,


    }
})();