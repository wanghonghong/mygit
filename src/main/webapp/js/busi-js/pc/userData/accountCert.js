/**
 * Created by whh on 2016/9/1.
 */
CommonUtils.regNamespace("account", "cert");
var  tabindex=0;
account.cert = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/user',
        url2:CONTEXT_PATH+'/join',
    }
    var _init = function(){
        _bind();
        _queryList(1);
    }
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
                            $("#msa_content").html(html);
                            _initAreaDistrict(index);
                            _showRoleWindow();
                            validate();
                        })
                    }
                }
            })
        }
        else{
            jumi.template(tpl_name,function(html){
                $("#msa_content").html(html);
            })
        }
    }
    //省市区插件初始化
    var _initAreaDistrict = function(index){
        if(index===2){
            var province = $("#_province").val();
            var city = $("#_city").val();
            var district = $("#_district").val();
            $(".distpicker").distpicker({
                province: province ,
                city: city ,
                district: district ,
            });
        }
        if(index===3){
            var province = $("#_province2").val();
            var city = $("#_city2").val();
            var district = $("#_district2").val();
            $(".distpicker").distpicker({
                province: province ,
                city: city ,
                district: district ,
            });
        }
    }
    //点击服务角色修改选择并展示
    var _showRoleWindow = function () {
            $("#service_modify_btn").click(function() {
                var elem = document.getElementById('dialoginfo');
                dialog({
                    title:'服务商角色',
                    content: elem,
                    okValue: '确定',
                    ok: function() {
                        var flag = _serviceRoleChange();
                        return flag;

                    },
                }).width(660).showModal();
            });
    }

    //服务角色修改申请弹窗选中后展示到页面
    var _serviceRoleChange = function () {
        var roles = [],flag = true;
        $('input[name="role"]:checked').each(function () {
            var obj = {};
            obj.id = $(this).val();
            obj.name = $(this).data('name');
            roles.push(obj);
        })
        var tpl = '';
        tpl =   ' <i class="iconfont icon-edit"></i>'+
            '<span class="service_role" data-id="'+roles[0].id+'"data-name="'+roles[0].name+'">'+roles[0].name+'</span>';
        $('#service_content').html(tpl);

    }
    //表单验证
    var validate = function () {
        var validate1 = $("#signupForm1").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                email:{
                    email:true,
                    required:true,
                },
                wxnum:"required",
                cardNum:{
                    isIdCardNo:true,
                    required:true,
                },
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"联系姓名不能少于2位"
                },
                email:{
                    email:"邮箱格式不正确",
                    required:"请输入邮箱"
                },
                wxnum:"请输入微信号",
                cardNum:{
                    isIdCardNo:"请输入正确的身份证号",
                    required:"身份证号不能为空",
                },
            }
        });
        var validate2 = $("#signupForm2").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                performance:"required",
                address:"required",
                companyName:{
                    required:true,
                },
                businessLicense:{
                    required:true,
                },
                manageNum:{
                    isInteger:true,
                    isIntGtZero:true,
                    required:true,
                },
                companyAddr:{
                    required:true,
                },
                companyUrl:{
                    required:true,
                },
                companyDesc:{
                    required:true,
                },
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"联系姓名不能少于2位"
                },
                performance:"请输入业绩",
                address:"请输入地址",
                companyName:{
                    required:"请输入公司名称",
                },
                businessLicense:{
                    required:"营业执照不能为空",
                },
                manageNum:{
                    isInteger:"请输入正确人数",
                    isIntGtZero:"人数必须大于0",
                    required:"请输入经营人数",
                },
                companyAddr:{
                    required:"请输入公司地址",
                },
                companyUrl:{
                    required:"请输入公司网址",
                },
                companyDesc:{
                    required:"请输入公司简介",
                },

            }
        });
        var validate3 = $("#signupForm3").validate({
            rules:{
                userName:{
                    required:true,
                    minlength:2
                },
                performance:"required",
                address:"required",
                companyName2:{
                    required:true,
                },
                businessLicense2:{
                    required:true,
                },
                manageNum2:{
                    isInteger:true,
                    isIntGtZero:true,
                    required:true,
                },
                companyAddr2:{
                    required:true,
                },
                companyUrl2:{
                    required:true,
                },
                companyDesc2:{
                    required:true,
                },
            },
            messages:{
                userName:{
                    required:"请输入联系姓名",
                    minlength:"联系姓名不能少于2位"
                },
                performance:"请输入业绩",
                address:"请输入地址",
                companyName2:{
                    required:"公司名称不能为空",
                },
                businessLicense2:{
                    required:"营业执照不能为空",
                },
                manageNum2:{
                    isInteger:"请输入正确人数",
                    isIntGtZero:"人数必须大于0",
                    required:"请输入经营人数",
                },
                companyAddr2:{
                    required:"公司地址不能为空",
                },
                companyUrl2:{
                    required:"公司网址不能为空",
                },
                companyDesc2:{
                    required:"公司简介不能为空",
                },
            }
        });
    }
    var _bind = function () {
            var userId = $("#userId").val();
            $("#account_tab li").unbind('click').click(function(){
                var element = $(this);
                element.addClass("active").siblings().removeClass("active");
                var target = element.attr("data-target");
                var index = element.data('index');
                $("#"+target).addClass("active").siblings().removeClass("active");
                _queryList(index);
            });
             _addImgEvent2("#m-add-img1");
             _addImgEvent("#m-add-img2");
             _addImgEvent("#m-add-img3");
             _addImgEvent("#m-add-img4");

            //表单验证通过后执行保存
            $('#msa_content').on('click','#save',function () {
                    var form = $("#signupForm1");
                    if (form.valid()) {
                        save();
                    } else {
                        $('body').animate({scrollTop:0},1000);
                    }
            })
            $('#msa_content').on('click','#save2',function () {
                var form = $("#signupForm2");
                if (form.valid()) {
                    save2();
                } else {
                    $('body').animate({scrollTop:0},1000);
                }
            })
            $('#msa_content').on('click','#save3',function () {
                var form = $("#signupForm3");
                if (form.valid()) {
                    save3();
                } else {
                    $('body').animate({scrollTop:0},1000);
                }
            })

            var save =function () {
                var phoneNumber = $("#phoneNumber").val();
                var userName = $("#userName").val();
                var email = $("#email").val();
                var wxnum = $("#wxnum").val();
                var cardNum = $("#cardNum").val();
                var sex = $("input:radio[name='sex']:checked").val();
                var headImg = $("#headImg").attr("src");
                var cardNumImg = $("#m-add-img2 img").attr("src");
                var data = {
                    userName: userName,
                    phoneNumber: phoneNumber,
                    email: email,
                    wxnum: wxnum,
                    cardNum: cardNum,
                    sex: sex,
                    headImg: headImg,
                    cardNumImg: cardNumImg
                }
                var jsonData = JSON.stringify(data);
                var url = ajaxUrl.url1 ;
                $.ajaxJsonPut(url, jsonData, {
                    done: function (res) {
                        if (res.code === 0) {
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'修改成功！',
                                isAutoDisplay:false,
                            });
                            dm.render();
                        } else {
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'修改失败！',
                                isAutoDisplay:false,
                            });
                            dm.render();
                        }
                    }
                });
            }

            var save2 =function () {
                var joinUo = {};
                joinUo.userId = userId;
                joinUo.userName = $("#userName").val();
                joinUo.performance = $("#performance").val();
                joinUo.address = $("#address").val();
                joinUo.companyName = $("#companyName").val();
                joinUo.businessLicense = $("#m-add-img3 img").attr("src");
                joinUo.manageNum = $("#manageNum").val();
                joinUo.province = $("#province").find("option:selected").val();
                joinUo.city = $("#city").find("option:selected").val();
                joinUo.district = $("#district").find("option:selected").val();
                joinUo.companyUrl = $("#companyUrl").val();
                joinUo.companyDesc = $("#companyDesc").val();
                joinUo.type = 1;
                joinUo.subType = $('#subType').val();
                var jsonData = JSON.stringify(joinUo);
                var url = ajaxUrl.url2;
                $.ajaxJsonPut(url, jsonData, {
                    done: function (res) {
                        if (res.code === 0) {
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'修改成功！',
                                isAutoDisplay:false,
                            });
                            dm.render();
                        } else {
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'修改失败！',
                                isAutoDisplay:false,
                            });
                            dm.render();
                        }
                    }
                });
            }

            var save3 =function () {
                var joinUo = {};
                joinUo.userId = userId;
                joinUo.userName = $("#userName").val();
                joinUo.performance = $("#performance").val();
                joinUo.address = $("#address").val();
                joinUo.companyName = $("#companyName2").val();
                joinUo.businessLicense = $("#m-add-img4 img").attr("src");
                joinUo.manageNum = $("#manageNum2").val();
                joinUo.province = $("#province2").find("option:selected").val();
                joinUo.city = $("#city2").find("option:selected").val();
                joinUo.district = $("#district2").find("option:selected").val();
                joinUo.companyUrl = $("#companyUrl2").val();
                joinUo.companyDesc = $("#companyDesc2").val();
                joinUo.type = 2;
                joinUo.subType = $('#subType').val();
                var json = [];
                $('#service_content').find('.service_role').each(function(){
                    var obj = {};
                    var doc = $(this);
                    obj.id = doc.data('id');
                    obj.name = doc.data('name');
                    json.push(obj);
                })
                joinUo.applyRole = json[0].id;
                var jsonData = JSON.stringify(joinUo);
                var url = ajaxUrl.url2;
                $.ajaxJsonPut(url, jsonData, {
                    done: function (res) {
                        if (res.code === 0) {
                            var dm = new dialogMessage({
                                type:1,
                                fixed:true,
                                msg:'修改成功！',
                                isAutoDisplay:false,
                            });
                            dm.render();
                        } else {
                            var dm = new dialogMessage({
                                type:2,
                                fixed:true,
                                msg:'修改失败！',
                                isAutoDisplay:false,
                            });
                            dm.render();
                        }
                    }
                });
            }

        }
    return {
        init :_init
    };
})()

function update() {

}
account.cert.nofind=function(){
    var img=event.srcElement;
    img.src=CONTEXT_PATH+"/css/pc/img/no_picture.png";
    //控制不要一直跳动
    img.onerror=null;
};

function ajaxPostlg(url,data,url2){
    $.ajaxJson(url,data,{
        done:function(respone){
            if(respone.data.code==0){
                window.location.href=url2;
            }else{
                alert(respone.data.cause);
            }
        }
    });
}
//图片弹出插件
var _addImgEvent=function (id) {
    $('#msa_content').on('click',id,function(){
        var d = new Dialog({
            multifile:false,
            context_path:CONTEXT_PATH, //请求路径,  必填
            resType:5 ,//图片1，视频2，语音3   必填
            height:380,
            width:600,
            callback:function(url){
                if(url){
                    $(id).find(".add").addClass("z-hide");
                    $(id).find("img").attr("src",url).removeClass("z-hide");
                    $(id).find("[name='groupImagePath']").val(url);
                }
            }
        });
        d.render();
    })
}
var _addImgEvent2=function (id) {
    $('#msa_content').on('click',id,function(){
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