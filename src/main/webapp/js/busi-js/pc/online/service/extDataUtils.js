CommonUtils.regNamespace('extDataUtils');
extDataUtils = (function(){
    var queryProductInfoUrl = CONTEXT_PATH+'/product/info/';

    var _getProductInfo = function(pid,callback){
        console.log('query product_info');
        $.ajaxJsonGet(queryProductInfoUrl+pid,null,{
            done:function(res){
                console.log('do query......');
                callback(res.data);
            }
        });
    }
    var _createProductInfo = function(target,pid,callback){
        console.log('ext fun doing......');
        _getProductInfo(pid,function(product){
            console.log('product:',product);
             var ext = {
                name:product.name,
                price:parseFloat(product.price/100).toFixed(2),
                imgUrl:product.picSquare,
                type:'product_info'
            };

            if(typeof callback ==='function'){
                callback(ext);
            }
        });

    };

    return {
        createProductInfo:_createProductInfo
    }

}());