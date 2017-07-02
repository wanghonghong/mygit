/**
 * Created by zx on 2017/5/10.
 */

CommonUtils.regNamespace("signup", "data");
signup.data = (function () {
    var _getdata = function () {
        var json = [
            {
                id: 1,
                name: "姓名",
                field: "userName",
                type: "input",
                placeholder:"请输入您的姓名"
            }, {
                id: 2,
                name: "手机",
                field: "telPhone",
                type: "input",
                placeholder:"您的联系方式"
            }, {
                id: 3,
                name: "性别",
                field: "sex",
                type: "radio",
                items: [
                    {
                        itemname: "男",
                        active:true
                    },
                    {
                        itemname: "女"
                    }
                ]
            }, {
                id: 4,
                name: "职位",
                field: "position",
                type: "input",
                placeholder:"请输入您的职位"
            }, {
                id: 5,
                name: "邮箱",
                field: "email",
                type: "input",
                placeholder:"请输入邮箱"
            }, {
                id: 6,
                name: "地址",
                field: "address",
                type: "input",
                placeholder:"请输入所在地区"
            }, {
                id: 7,
                name: "网址",
                field: "webUrl",
                type: "input",
                placeholder:"请输入网址"
            }, {
                id: 8,
                name: "公司",
                field: "company",
                type: "input",
                placeholder:"请输入您的公司"
            }
        ]

        return json;
    }
    return {
        getdata: _getdata

    }
})();