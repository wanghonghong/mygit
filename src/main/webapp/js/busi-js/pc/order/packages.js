CommonUtils.regNamespace('delivery','packages');
delivery.packages = (function(){
    "use strict";
    var companyList;
    var packageList;
    var orderInfoId;
    var packageCount = 1;
    var orderDetails = [];
    var packageDetail  = {};
    var orderDeliverys = {};
    var productDetail = {};

    var Package= function(data){
        this.orderDetails = orderDetails;
        this.companys = companyList;
        this.packageId = packageCount++;
        this.form;
        if(data){
            this.first = data.first;
        }
        packageDetail[this.packageId] = this;

    };
    Package.prototype.addPackage = function(){
        var pack = this;
        var data = {
            packageId:this.packageId,
            orderDetails:this.orderDetails,
            companys:this.companys,
            package:pack,
            checkboxFun:'checkDetail',
            productCount:'{{orderDetail.length}}',
            remarkCount:'{{deliveryNote.length}}'
        };
        var first = this.first;
        jumi.template('/order/pc/packages',data,function(tpl){
            $('.addPackages').before(tpl);
            var all = false;
            var orderDetail = [];
            var pack = false;
            if(first){
                all = true;
                for(var i in orderDetails){
                    orderDetail.push(orderDetails[i].orderDetailId+"");
                }
                pack = true;
            }
             data.package.form = new Vue({
                el:"#package"+data.packageId,
                props:['transNumber','transCompany'],
                data:{
                    transCode:'',
                    transNumber:'',
                    deliveryNote:'',
                    transCompany:'',
                    orderDetail:orderDetail,
                    all:all,
                    pack:pack,
                    packageId:data.packageId
                },
                 methods:{
                    checkDetail:function(event){
                        var orderDetailId = $(event.currentTarget).prev().val();
                        var packageId = this.packageId;
                        var details = this.orderDetail;
                        console.log(details);
                        //获取用户的点击的label的packageDetail 判断其他包裹内是否有此商品
                        console.log('packageId:',packageId);
                        if( details.indexOf(orderDetailId)==-1){
                            $.each(packageDetail,function(index,pack){
                                if(pack.packageId !== orderDetailId){
                                    for(var i in pack.form.orderDetail){
                                        if(pack.form.orderDetail[i]===orderDetailId){
                                            var dm = new dialogMessage({
                                                type:2,
                                                fixed:true,
                                                msg:'请先取消其他包裹内的同件商品',
                                                isAutoDisplay:true,
                                                time:3000
                                            });
                                            dm.render();
                                            if (event) {
                                                event.preventDefault();
                                            }
                                            //console.log(pack.form.orderDetail);
                                        }
                                    }
                                    //去掉其他地方的全选
                                    //pack.form.all = false;
                                }
                            });
                        }else{
                            this.all = false;
                        }
                    }
                 }
            });
            _bindEvent(data);

        });
    };
    var _bindEvent = function(data){
        var id = data.packageId;
        $('.del'+id).bind('click',function(){
            $('#package'+id).remove();
            delete packageDetail[id];
            console.log(packageDetail);
        });
        $('#transCompany'+id).select2({
            theme:'jumi'
        });
        $('#transCompany'+id).on("change", function (e) {
            var val = $(this).val();
            var vals = val.split(",");
            var transCompany = vals[0];
            var transCode = vals[1];
            packageDetail[id].form.transCode = transCode;
            packageDetail[id].form.transCompany = transCompany;
            console.log(packageDetail[id].form.transCompany);
        });
        var selectAll = $('.u-countinfo > div').find('label');
        $(selectAll).unbind('click');
        $(selectAll).bind('click',function(){
            var packageId = $(this).attr('pack-id');
            var details = packageDetail[packageId];
            if(!details.form.all){
                // 清除页面上其他的全选 和 商品
                $.each(packageDetail,function(index,pack){
                    pack.form.orderDetail.splice(0,pack.form.orderDetail.length);
                    pack.form.all = false;
                    pack.form.pack = false;
                });
                for(var i in orderDetails){
                    var detailId = orderDetails[i].orderDetailId;
                    details.form.orderDetail.push(detailId+"");
                }
                details.form.pack = true;
            }else{
                details.form.orderDetail = [];
                details.form.pack = false;
            }
        });

    };

    var initSendPackage = function(){
        for(var i in orderDeliverys){
            var orderDelivery = orderDeliverys[i];
            console.log(orderDelivery);
            var pack = orderDelivery[0];
            var data ={
                packageId:packageCount++,
                orderDetails:orderDeliverys[i],
                transCompany:pack.transCompany,
                transCode:pack.transCode,
                transNumber:pack.transNumber,
                deliveryNote:pack.deliveryNote,
                isSend:true
            };
            jumi.template(STATIC_URL+'/order/pc/packages',data,function(tpl) {

                $('.addPackages').before(tpl);
            });
        }
    };

    var _sendAllPackage = function(){

    };

    var _getPackage = function(pack){
        var packForm;
        var message;
        if(pack.pack){
            packForm = {};
            if(!pack.transNumber||pack.transNumber===''){
               message = "包裹"+pack.packageId+"的物流单号未填写";
            }
            packForm.transNumber= pack.transNumber;
            packForm.deliveryNote = pack.deliveryNote;
            if(!pack.transCompany||pack.transCompany===''){
                message = "包裹"+pack.packageId+"的物流公司未选择";
            }
            packForm.transCompany = pack.transCompany;
            packForm.orderDetailId = pack.orderDetail;
            //packForm.orderDetailCo = productDetail[pack.orderDetail];
            if(pack.orderDetail.length<1){
                message = "包裹"+pack.packageId+"未选择任何商品";
            }
            packForm.transCode = pack.transCode;

        }
        if(message){
            var dm = new dialogMessage({
                type:2,
                fixed:true,
                msg:message,
                isAutoDisplay:true,
                time:3000
            });
            dm.render();
            return null;
        }
        return packForm;
    }

    var _savePackages = function(){
        var packs = {
            orderInfoId:orderInfoId,
            orderDetailAndSendsVos:[]
        };
        for(var i in packageDetail){
            var pack = packageDetail[i].form;
            var packForm = _getPackage(pack);
            if(packForm){
                packs.orderDetailAndSendsVos.push(packForm);
            }
        }
        return packs;
    };

    var _addPackage = function(){

    };

    var _delPackage = function(){

    };

    var resetPackages = function(){
        packageDetail  = {};
        packageCount = 1;
        orderDetails = [];
        orderDeliverys = {};
    }

    var _initData = function(data){
        companyList = data.companys;
        for(var i in data.orderDetails){
            if(!data.orderDetails[i].orderDeliveryId){
                orderDetails.push(data.orderDetails[i]);
            }else{
                var devId = data.orderDetails[i].orderDeliveryId;
                if(!orderDeliverys[devId]){
                    orderDeliverys[devId] = [];
                }
                orderDeliverys[devId].push(data.orderDetails[i]);
            }
            productDetail[data.orderDetails[i].orderDetailId] = {
                pid:data.orderDetails[i].pid,
                productSpecId:data.orderDetails[i].productSpecId,
                orderDetailId:data.orderDetails[i].orderDetailId
            }
        }
        console.log('orderDetails:',orderDetails);
        console.log('orderDeliverys',orderDeliverys);
        //orderDetails = data.orderDetails;
        orderInfoId = data.orderInfoId;
        initSendPackage();
    };

    return {
        savePackage:_savePackages,
        addPackage:_addPackage,
        delPackage:_delPackage,
        initData:_initData,
        Package:Package,
        reset:resetPackages
    }

})();
