/**
 * Created by wxz on 2016/11/07
 * QQ表情
 */
CommonUtils.regNamespace("qq", "face");
qq.face = (function () {
    //初始化
    var _init = function () {
       // jumi.file("qqface.json", call_backface);
    };
//读取表情
   /* function call_backface(res){
       var ptarr = res["data"].message;
        for(var i=0;i<ptarr.length;i++)
        {
            var html = $("#faceli").clone();
            html.css("display", "");
            html.find("img").attr("src",CONTEXT_PATH+"/css/pc/img/qqface/"+ptarr[i].pic);
            html.find("img").attr("alt","["+ptarr[i].text+"]");
            html.removeAttr("id");
            $("#faceul").append(html);
            _read_content();
        }
    }*/

    //表情显示、隐藏   btnId--表情按钮 containerId--容器
    var _read_file = function (btnId,containerId,conId) {

        var tag =0;
        $(containerId).hide();
        localStorage.setItem(containerId,tag);
        $(btnId).unbind('click').bind('click',function () {
            tag = localStorage.getItem(containerId);
            if(tag==="0") {
                jumi.template('common/qqface/face',function (tpl) {
                    $(containerId).html(tpl);
                });

                $(containerId).show();
                tag=1;
                localStorage.setItem(containerId,tag);
                _read_content(containerId,conId);
            }
            else {
                $(containerId).hide();
                tag=0;
                localStorage.setItem(containerId,tag);
            }
            $(conId).focus(function () {  //输入框获取焦点时隐藏
                $(containerId).hide();
                tag=0;
                localStorage.setItem(containerId,tag);
            });
        });

    }
   //文本框填充表情
    var _read_content =  function (containerId,conId) {
        var obj = containerId.replace("#","");
        $("div[id='"+obj+"'] div > div > ul").find("li").unbind('click').bind('click',function() {
            localStorage.setItem(containerId, 0);
            var text = $(this).find("img").attr("alt");
            var obj = $(conId);
            text = obj.val() + text;
            obj.val(text);
            // 隐藏表情包并取焦点
            $(containerId).hide();
            $(conId).focus();
            //
        });
    }

    var _map = function(keys){
        var faces = [];
        if(!keys){
            return faces;
        }else if(typeof keys === 'string'){
            faces.push(wxface.map[keys]);
        }else if(Array.isArray(keys)){
            for(var i in keys){
                faces.push(wxface.map[keys[i]]);
            }
        }
        return faces;
    };
    var _matchFaceCode  = function(str){
        var reg = /\/::\)|\/::~|\/::B|\/::\||\/:8-\)|\/::<|\/::$|\/::X|\/::Z|\/::\'\(|\/::-\||\/::@|\/::P|\/::D|\/::O|\/::\(|\/::\+|\/:--b|\/::Q|\/::T|\/:,@P|\/:,@-D|\/::d|\/:,@o|\/::g|\/:\|-\)|\/::!|\/::L|\/::\>|\/::,@|\/:,@f|\/::-S|\/:\?|\/:,@x|\/:,@@|\/::8|\/:,@\!|\/:\!\!\!|\/:xx|\/:bye|\/:wipe|\/:dig|\/:handclap|\/:&-\(|\/:B-\)|\/:<@|\/:@>|\/::-O|\/:>-\||\/:P\-\(\|\/::\'\||\/:X-\)|\/::\*|\/:@x|\/:8\*|\/:pd|\/:\<W\>|\/:beer|\/:basketb|\/:oo|\/:coffee|\/:eat|\/:pig|\/:rose|\/:fade|\/:showlove|\/:heart|\/:break|\/:cake|\/:li|\/:bome|\/:kn|\/:footb|\/:ladybug|\/:shit|\/:moon|\/:sun|\/:gift|\/:hug|\/:strong|\/:weak|\/:share|\/:v|\/:@\)|\/:jj|\/:@@|\/:bad|\/:lvu|\/:no|\/:ok|\/:love|\/:\<L\>|\/:jump|\/:shake|\/:\<O\>|\/:circle|\/:kotow|\/:turn|\/:skip|\/:oY|\/:#-0|\/:hiphot|\/:kiss|\/:\<&|\/:&\>/;
        while(str.search(reg)>=0){
            var code = str.match(reg);
            var facePath = wxface.path+wxface.map[code].png;
            var faceImg = '<img class="icon-face" src='+facePath+' />';
            str = str.replace(reg,faceImg);
        }
        return str;
    };
    var _faceDialog = function(obj,cssOptions,callback){
        try{
            var container = $(obj).find('.face-container');
            if($(container)[0].hasChildNodes()){
                var event = window.event;
                var target =event.target;
                var key = $(target).attr('key');
                var obj = wxface.map[key];
                var display = $(container).css('display');
                if(!display||display===''||display==='block'){
                    $(container).hide();
                }else if(display==='none'){
                    $(container).show();
                }
                if(typeof callback==='function'){
                    callback(obj);
                }
            }else{
                jumi.template('common/qqface/face',function (tpl) {
                    $(container).html(tpl);
                    if(cssOptions){
                        var qq = $(container).find('.qq');
                        for(var i in cssOptions){
                            $(qq).css(i,cssOptions[i]);
                        }
                    }
                });

            }
            window.onmousedown  = function(e){
                var target = e.target;
                if($(target).parents('.face-container').length<1){
                    $(container).hide();
                }
                window.onmousedown = null;
            }
        }catch(e){
            console.error(e);
        }
    };
    var _bindEvent = function(container,callback){
        var lis = $('#faceul').find('li');
            lis.bind('click',function(){
                var key = $(this).find('img').attr('key');
                var obj = wxface.map[key];
                callback(obj);
        });
    };
    var wxface = {
        map:{
            "/::)":{"pic":"0.gif","text":"微笑","code":"/::)","png":"0.png"},
            "/::~":{"pic":"1.gif","text":"撇嘴","code":"/::~","png":"1.png"},
            "/::B":{"pic":"2.gif","text":"色","code":"/::B","png":"2.png"},
            "/::|":{"pic":"3.gif","text":"发呆","code":"/::|","png":"3.png"},
            "/:8-)":{"pic":"4.gif","text":"得意","code":"/:8-)","png":"4.png"},
            "/::<":{"pic":"5.gif","text":"流泪","code":"/::<","png":"5.png"},
            "/::$":{"pic":"6.gif","text":"害羞","code":"/::$","png":"6.png"},
            "/::X":{"pic":"7.gif","text":"闭嘴","code":"/::X","png":"7.png"},
            "/::Z":{"pic":"8.gif","text":"睡","code":"/::Z","png":"8.png"},
            "/::'(":{"pic":"9.gif","text":"大哭","code":"/::'(","png":"9.png"},
            "/::-|":{"pic":"10.gif","text":"尴尬","code":"/::-|","png":"10.png"},
            "/::@":{"pic":"11.gif","text":"发怒","code":"/::@","png":"11.png"},
            "/::P":{"pic":"12.gif","text":"调皮","code":"/::P","png":"12.png"},
            "/::D":{"pic":"13.gif","text":"呲牙","code":"/::D","png":"13.png"},
            "/::O":{"pic":"14.gif","text":"惊讶","code":"/::O","png":"14.png"},
            "/::(":{"pic":"15.gif","text":"难过","code":"/::(","png":"15.png"},
            "/::+":{"pic":"16.gif","text":"酷","code":"/::+","png":"16.png"},
            "/:–b":{"pic":"17.gif","text":"冷汗","code":"/:–b","png":"17.png"},
            "/::Q":{"pic":"18.gif","text":"抓狂","code":"/::Q","png":"18.png"},
            "/::T":{"pic":"19.gif","text":"吐","code":"/::T","png":"19.png"},
            "/:,@P":{"pic":"20.gif","text":"偷笑","code":"/:,@P","png":"20.png"},
            "/:,@-D":{"pic":"21.gif","text":"可爱","code":"/:,@-D","png":"21.png"},
            "/::d":{"pic":"22.gif","text":"白眼","code":"/::d","png":"22.png"},
            "/:,@o":{"pic":"23.gif","text":"傲慢","code":"/:,@o","png":"23.png"},
            "/::g":{"pic":"24.gif","text":"饥饿","code":"/::g","png":"24.png"},
            "/:|-)":{"pic":"25.gif","text":"困","code":"/:|-)","png":"25.png"},
            "/::!":{"pic":"26.gif","text":"惊恐","code":"/::!","png":"26.png"},
            "/::L":{"pic":"27.gif","text":"流汗","code":"/::L","png":"27.png"},
            "/::>":{"pic":"28.gif","text":"憨笑","code":"/::>","png":"28.png"},
            "/::,@":{"pic":"29.gif","text":"大兵","code":"/::,@","png":"29.png"},
            "/:,@f":{"pic":"30.gif","text":"奋斗","code":"/:,@f","png":"30.png"},
            "/::-S":{"pic":"31.gif","text":"咒骂","code":"/::-S","png":"31.png"},
            "/:?":{"pic":"32.gif","text":"疑问","code":"/:?","png":"32.png"},
            "/:,@x":{"pic":"33.gif","text":"嘘","code":"/:,@x","png":"33.png"},
            "/:,@@":{"pic":"34.gif","text":"晕","code":"/:,@@","png":"34.png"},
            "/::8":{"pic":"35.gif","text":"折磨","code":"/::8","png":"35.png"},
            "/:,@!":{"pic":"36.gif","text":"衰","code":"/:,@!","png":"36.png"},
            "/:!!!":{"pic":"37.gif","text":"骷髅","code":"/:!!!","png":"37.png"},
            "/:xx":{"pic":"38.gif","text":"敲打","code":"/:xx","png":"38.png"},
            "/:bye":{"pic":"39.gif","text":"再见","code":"/:bye","png":"39.png"},
            "/:wipe":{"pic":"40.gif","text":"擦汗","code":"/:wipe","png":"40.png"},
            "/:dig":{"pic":"41.gif","text":"抠鼻","code":"/:dig","png":"41.png"},
            "/:handclap":{"pic":"42.gif","text":"鼓掌","code":"/:handclap","png":"42.png"},
            "/:&-(":{"pic":"43.gif","text":"糗大了","code":"/:&-(","png":"43.png"},
            "/:B-)":{"pic":"44.gif","text":"坏笑","code":"/:B-)","png":"44.png"},
            "/:<@":{"pic":"45.gif","text":"左哼哼","code":"/:<@","png":"45.png"},
            "/:@>":{"pic":"46.gif","text":"右哼哼","code":"/:@>","png":"46.png"},
            "/::-O":{"pic":"47.gif","text":"哈欠","code":"/::-O","png":"47.png"},
            "/:>-|":{"pic":"48.gif","text":"鄙视","code":"/:>-|","png":"48.png"},
            "/:P-(":{"pic":"49.gif","text":"委屈","code":"/:P-(","png":"49.png"},
            "/::'|":{"pic":"50.gif","text":"快哭了","code":"/::'|","png":"50.png"},
            "/:X-)":{"pic":"51.gif","text":"阴险","code":"/:X-)","png":"51.png"},
            "/::*":{"pic":"52.gif","text":"亲亲","code":"/::*","png":"52.png"},
            "/:@x":{"pic":"53.gif","text":"吓","code":"/:@x","png":"53.png"},
            "/:8*":{"pic":"54.gif","text":"可怜","code":"/:8*","png":"54.png"},
            "/:pd":{"pic":"55.gif","text":"菜刀","code":"/:pd","png":"55.png"},
            "/:<W>":{"pic":"56.gif","text":"西瓜","code":"/:<W>","png":"56.png"},
            "/:beer":{"pic":"57.gif","text":"啤酒","code":"/:beer","png":"57.png"},
            "/:basketb":{"pic":"58.gif","text":"篮球","code":"/:basketb","png":"58.png"},
            "/:oo":{"pic":"59.gif","text":"乒乓","code":"/:oo","png":"59.png"},
            "/:coffee":{"pic":"60.gif","text":"咖啡","code":"/:coffee","png":"60.png"},
            "/:eat":{"pic":"61.gif","text":"饭","code":"/:eat","png":"61.png"},
            "/:pig":{"pic":"62.gif","text":"猪头","code":"/:pig","png":"62.png"},
            "/:rose":{"pic":"63.gif","text":"玫瑰","code":"/:rose","png":"63.png"},
            "/:fade":{"pic":"64.gif","text":"凋谢","code":"/:fade","png":"64.png"},
            "/:showlove":{"pic":"65.gif","text":"示爱","code":"/:showlove","png":"65.png"},
            "/:heart":{"pic":"66.gif","text":"爱心","code":"/:heart","png":"66.png"},
            "/:break":{"pic":"67.gif","text":"心碎","code":"/:break","png":"67.png"},
            "/:cake":{"pic":"68.gif","text":"蛋糕","code":"/:cake","png":"68.png"},
            "/:li":{"pic":"69.gif","text":"闪电","code":"/:li","png":"69.png"},
            "/:bome":{"pic":"70.gif","text":"炸弹","code":"/:bome","png":"70.png"},
            "/:kn":{"pic":"71.gif","text":"刀","code":"/:kn","png":"71.png"},
            "/:footb":{"pic":"72.gif","text":"足球","code":"/:footb","png":"72.png"},
            "/:ladybug":{"pic":"73.gif","text":"瓢虫","code":"/:ladybug","png":"73.png"},
            "/:shit":{"pic":"74.gif","text":"便便","code":"/:shit","png":"74.png"},
            "/:moon":{"pic":"75.gif","text":"月亮","code":"/:moon","png":"75.png"},
            "/:sun":{"pic":"76.gif","text":"太阳","code":"/:sun","png":"76.png"},
            "/:gift":{"pic":"77.gif","text":"礼物","code":"/:gift","png":"77.png"},
            "/:hug":{"pic":"78.gif","text":"拥抱","code":"/:hug","png":"78.png"},
            "/:strong":{"pic":"79.gif","text":"强","code":"/:strong","png":"79.png"},
            "/:weak":{"pic":"80.gif","text":"弱","code":"/:weak","png":"80.png"},
            "/:share":{"pic":"81.gif","text":"握手","code":"/:share","png":"81.png"},
            "/:v":{"pic":"82.gif","text":"胜利","code":"/:v","png":"82.png"},
            "/:@)":{"pic":"83.gif","text":"抱拳","code":"/:@)","png":"83.png"},
            "/:jj":{"pic":"84.gif","text":"勾引","code":"/:jj","png":"84.png"},
            "/:@@":{"pic":"85.gif","text":"拳头","code":"/:@@","png":"85.png"},
            "/:bad":{"pic":"86.gif","text":"差劲","code":"/:bad","png":"86.png"},
            "/:lvu":{"pic":"87.gif","text":"爱你","code":"/:lvu","png":"87.png"},
            "/:no":{"pic":"88.gif","text":"NO","code":"/:no","png":"88.png"},
            "/:ok":{"pic":"89.gif","text":"OK","code":"/:ok","png":"89.png"},
            "/:love":{"pic":"90.gif","text":"爱情","code":"/:love","png":"90.png"},
            "/:<L>":{"pic":"91.gif","text":"飞吻","code":"/:<L>","png":"91.png"},
            "/:jump":{"pic":"92.gif","text":"跳跳","code":"/:jump","png":"92.png"},
            "/:shake":{"pic":"93.gif","text":"发抖","code":"/:shake","png":"93.png"},
            "/:<O>":{"pic":"94.gif","text":"怄火","code":"/:<O>","png":"94.png"},
            "/:circle":{"pic":"95.gif","text":"转圈","code":"/:circle","png":"95.png"},
            "/:kotow":{"pic":"96.gif","text":"磕头","code":"/:kotow","png":"96.png"},
            "/:turn":{"pic":"97.gif","text":"回头","code":"/:turn","png":"97.png"},
            "/:skip":{"pic":"98.gif","text":"跳绳","code":"/:skip","png":"98.png"},
            "/:oY":{"pic":"99.gif","text":"挥手","code":"/:oY","png":"99.png"},
            "/:#-0":{"pic":"100.gif","text":"激动","code":"/:#-0","png":"100.png"},
            "/:hiphot":{"pic":"101.gif","text":"街舞","code":"/:hiphot","png":"101.png"},
            "/:kiss":{"pic":"102.gif","text":"献吻","code":"/:kiss","png":"102.png"},
            "/:<&" :{"pic":"103.gif","text":"左太极","code":"/:<&","png":"103.png"},
            "/:&>":{"pic":"104.gif","text":"右太极","code":"/:&>","png":"104.png"}
        },
        path:THIRD_URL+"/css/pc/img/qqface/"
    }



    return {
        read_file:_read_file,
        faceDialog:_faceDialog,
        wxface:wxface,
        match:_matchFaceCode
    };
})();