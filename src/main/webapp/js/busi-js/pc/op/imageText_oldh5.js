/**
 * Created by zx on 2017/3/2.
 */

CommonUtils.regNamespace("imageText", "oldh5");
imageText.oldh5 = (function () {
    var count = 3;
    var sumH5img = 0;
    opt = {
        id: 0,
        tplPath: STATIC_URL + '/tpl/op/new/',
        imageTextTpyeAjax:CONTEXT_PATH+'/image_text_type/type_id/'
    }
    var _init = function (data) {
        delete  opt.imgTextRo;
        delete opt.audioRes;
        delete opt.buttonList;
        delete opt.imageTextTypes;
        delete opt.imgResList;
        delete opt.videoRes;
        delete opt.videoResoure;
        $.extend(opt, data);
        opt.typeId = data.typeId;
        if (!opt.buttonList) {
            opt.buttonList = [];
            opt.buttonList.push({"buttonName": "", "linkPath": ""})
            opt.buttonList.push({"buttonName": "", "linkPath": ""})
        }
        if (opt.imgTextRo) {
            opt.imgTextRo.colorId = opt.imgTextRo.colorId || 1;
        } else {
            opt.imgTextRo = {
                colorId: 1
            }
        }
        console.log(opt)
        initpage().then(function () {
            return getTypeList();
        }).then(function () {
            _initValue();
        }).then(function () {
            _bindEvent();
        })
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
    var initpage = function () {
        var dfdPlay = $.Deferred();
        var html = jumi.templateHtml('h5ImageText.html', opt, opt.tplPath);
        $("#Imageh5version").append(html);
        dfdPlay.resolve(); // 动画完成
        return dfdPlay;
    }


    var getTypeList=function () {
        var dfdPlay = $.Deferred();
        var typeurl=opt.imageTextTpyeAjax+opt.typeId;
        $.ajaxJsonGet(typeurl,null,{
            "done":function (res) {
                var tpl = '';
                var data = res.data;
                _.map(data.imageTextTypeList,function(k,v){
                    tpl+='<option value="'+k.id+'">'+k.typeName+'</option>'
                });
                $("#imageTextType").html(tpl);
                var selecttmp= $("#imageTextType").select2({
                    theme: "jumi"
                });
                if(opt.imgTextRo.imageTextType){
                    selecttmp.val(opt.imgTextRo.imageTextType).trigger("change");
                }else{
                    selecttmp.val(data.imageTextTypeList[0].id).trigger("change");
                }
                dfdPlay.resolve(); // 动画完成
            }
        });
        return dfdPlay;
    }

    var _initValue = function () {
        if (opt.imgTextRo) {
        }
        if(opt.imgResList){
            var imgResArr=[];
            $.each(opt.imgResList,function (i) {
                 console.log(opt.imgResList[i]);
                imgResArr.push(opt.imgResList[i].id);
                $("<li>",{
                    "class":"img"
                }).data(opt.imgResList[i]).html("<img src='"+opt.imgResList[i].resUrl+"' /><i class='iconfont icon-delete3 del'></i>").insertBefore("#imgResAdd")
            })
            var imgResIds=imgResArr.toString();
            $("#h5Template [name='imgResIds']").val(imgResIds);
        }
    }

    var _bindEvent=function () {
         $("#h5Template [name='imageTextType']").select2({
            theme: "jumi"
        })
        
        $("#chooseAudio").click(function () {
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:3 ,//图片1，视频2，语音3   必填,
                callback:function(url,node){
                      console.log(node);
                    if(!$.isEmptyObject(url)){
                        $("#audioResId").val(node.id);
                        $("#audioResName").val(node.resName);
                    }
                }
            });
            d.render();
        })
        $(".m-stylecolorbox  input:checkbox").click(function () {
            $(".m-stylecolorbox  input:checkbox").removeProp("checked");
            $(this).prop("checked","checked");
        })
        $("#h5Template .mianImg").click(function () {
            var _this=$(this);
            var d = new Dialog({
                context_path: CONTEXT_PATH,
                resType: 1,
                callback: function (url) {
                    if(!$.isEmptyObject(url)){
                      var  url=  jumi.picParse(url,720);
                        _this.attr("src",url);
                        $("#h5Template [name='imageUrl']").val(url)
                    }
                }
            });
            d.render();
        })

        $("#buttonlist").delegate(".linkbox .link", "click", function () {
            var shopId = $('#shopId').val();
            var element = $(this);
            menuDialog.Menu.initPage({
                shop_id:shopId,
            },function(menu){
                menu.getUrl(function(link){
                    var url;
                    switch(link.link_type){
                        case '1':
                            url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                            break;
                        case '2':
                            url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                            break;
                        case '3':
                            url = CONTEXT_PATH+link.link_url+"?shopId="+link.shop_id;
                    }
                    element.parent().find('input[name="linkPath"]').val(url);
                    element.parent().find('input[name="linkPath"]').attr('disabled',true);
                });
            });
        })
        $("#imgResAdd").click(function () {
            var _this=$(this);
            if( $("#h5Template .imgResList li.img").length>=12){
                var dm = new dialogMessage({
                    type:3,
                    msg:"图片最多选择12张！",
                    isAutoDisplay:true,
                    time:1500
                });
                dm.render();
                return;
            }
            var d = new Dialog({
                context_path: CONTEXT_PATH,
                resType: 1,
                callback: function (url,res) {
                    if(!$.isEmptyObject(url)){
                        var  url=  jumi.picParse(url,720);
                        _this.attr("src",url);
                        $("<li>",{
                            "class":"img"
                        }).data(res).html("<img src='"+res.resUrl+"' /><i class='iconfont icon-delete3 del'></i>").insertBefore("#imgResAdd")
                        updataImgResIds();
                    }
                }
            });
            d.render();
        })


        $("#h5Template ").delegate(".imgResList li img", "click", function () {
            var li = $(this).parent();
            var _this = $(this);
            var d = new Dialog({
                context_path: CONTEXT_PATH,
                resType: 1,
                callback: function (url,res) {
                    if(!$.isEmptyObject(url)){
                        var  url=  jumi.picParse(url,720);
                          li.data(res);
                        _this.attr("src",url);
                        updataImgResIds();
                    }
                }
            });
            d.render();
        });
        $("#h5Template ").delegate(".imgResList li .del", "click", function () {
            var li = $(this).parent();
            li.remove();
            updataImgResIds();
        });
        $("#h5Template ").delegate(".imgResList li .del", "click", function () {
            var li = $(this).parent();
            li.remove();
            updataImgResIds();
        });

    }

    var _getH5Info=function () {
         var data={};
         $("input[type='text'],input[type='hidden'],select,textarea","#h5From").each(function () {
             if( $(this).attr("name")){
                 if($(this).val()){
                     data[$(this).attr("name")]=$(this).val();
                 }
             }
         })
        data.colorId=$("input[name='stylecolor']:checked").val();
        data.formatCode=3;

        return data;
    }
    var _getImageTextRelateList =function () {
        var imageTextRelateList=[];
        $(".linkbox","#buttonlist").each(function () {
            var _this=$(this);
            var button={
                id:$("input[name='id']",_this).val(),
                linkPath:$("input[name='linkPath']",_this).val(),
                buttonName:$("input[name='buttonName']",_this).val()
            };
            imageTextRelateList.push(button);
        })
        return imageTextRelateList;
    }


    var updataImgResIds=function(){
        var imgResArr=[];
        $("#h5Template .imgResList li.img").each(function (i) {
            var data=$(this).data()
            imgResArr.push(data.id);
        })
        var imgResIds=imgResArr.toString();
        $("#h5Template [name='imgResIds']").val(imgResIds);
    }


    var _verifyData=function () {
        var sign=true;
        var imageTextType=$("#h5Template [name='imageTextType']").val();
        if($.isEmptyObject(imageTextType)){
            alertinfo("请选择一个图文分类");
            return false;
        }
        var imageTextTile=$("#h5From input[name='imageTextTile']").val();
        if($.isEmptyObject(imageTextTile)){
            alertinfo("请输入图文名称");
            return false;
        }
        var imageUrl=$("#h5From input[name='imageUrl']").val();
        if($.isEmptyObject(imageUrl)){
            alertinfo("主图不能为空");
            return false;
        }
        var shareText=$("#h5From textarea[name='shareText']").val();
        if($.isEmptyObject(shareText)){
            alertinfo("请输入分享语摘要");
            return false;
        }

        var imgResIds=$("#h5From input[name='imgResIds']").val();
        if($.isEmptyObject(imgResIds)){
            alertinfo("内容图片不能为空");
            return false;
        }

        return sign;
    }
    return {
        init: _init,
        verifyData:_verifyData,
        getH5Info:_getH5Info,
        getImageTextRelateList:_getImageTextRelateList
    };
})();
