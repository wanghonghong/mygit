/**
 * Created by whh on 2016/12/15.
 */
CommonUtils.regNamespace("community", "data");
community.data = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/community',
        url2: CONTEXT_PATH + '/community/posts',
    };
    var _init = function () {
        _bind();
    };
    //列表事件方法
    var _queryList = function(index){
        var tpl_name,url;
        tpl_name = $("#tabcontent_"+index).attr('data-tpl-name');
        url = $("#tabcontent_"+index).data('url');
        _queryListData(index,tpl_name,url);
    }
    //渲染数据
    var _queryListData = function(index,tpl_name,url){
        if (url) {
            url = CONTEXT_PATH + url;
            $.ajaxJsonGet(url, null, {
                done: function (res) {
                    if(res.code===0){
                        var data = res.data;
                        jumi.template(tpl_name, data, function (html) {
                            $("#content").html(html);
                            selectArea();
                            jumi.Select('#staff');
                            jumi.Select('#sex');
                            _addImgEvent1();
                        })
                    }
                }
            })
        }
        else{
            jumi.template(tpl_name,function(html){
                $("#content").html(html);
                if (index==3){
                    _postList;
                }
            })
        }
    }
    var _bind = function(){
        _queryList(1);

        var tabul = $(".m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
            _queryList($(this).data('index'));
        });
        _postList();

    }
    //省市区插件
    var selectArea = function () {
        $('.distpicker').distpicker({
            province:'-- 省 --',
            city: '-- 市 --',
            district:'-- 区 --',
            autoSelect: false
        });
    }

    community.index.nofind=function(){
        var img=event.srcElement;
        img.src=CONTEXT_PATH+"/css/pc/img/no_picture.png";
        //控制不要一直跳动
        img.onerror=null;
    };

    var _addImgEvent1=function () {
       $('#m-add-img1').click(function () {
           var d = new Dialog({
               multifile:false,
               context_path:CONTEXT_PATH, //请求路径,  必填
               resType:5 ,//图片1，视频2，语音3   必填
               height:380,
               width:600,
               callback:function(url){
                   if(url){
                       $("#headImg").find(".add").addClass("z-hide");
                       $("#headImg").attr("src",url).removeClass("z-hide");
                       $("#headImg").find("[name='groupImagePath']").val(url);
                   }
               }
           });
           d.render();
       })
    }

    var _save = function () {
        var communityCo = {};
        communityCo.headImg = $('#headImg').attr('src');
        communityCo.nickname = $('#nickname').val();
        communityCo.sex = $('#sex').find('option:selected').val();
        communityCo.phoneNumber = $('#phoneNumber').val();
        communityCo.staff = $('#staff').find('option:selected').val();
        communityCo.qq = $('#qq').val();
        communityCo.province = $('#province').find('option:selected').val();
        communityCo.city = $('#city').find('option:selected').val();
        communityCo.district = $('#district').find('option:selected').val();
        var jsonData = JSON.stringify(communityCo);
        var url = ajaxUrl.url1;
        $.ajaxJson(url,jsonData,{
            "done" : function (res) {
                if(res.code==0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'保存成功！',
                        isAutoDisplay:false,
                    });
                    dm.render();
                }
            }
        })
    }

    var _postList = function () {
            var url = ajaxUrl.url2;
            var params = {
                pageSize:5,
            };
            jumi.pagination('#pageToolbar',url,params,function(res,curPage){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    };
                    jumi.template('community/data_post_list',data,function(tpl){
                        $('#postList').html(tpl);
                    })
                }
            })
    }

    return {
        init :_init,
        save:_save,
        postList:_postList,
    };
})();