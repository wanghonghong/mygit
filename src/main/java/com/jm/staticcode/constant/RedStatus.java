package com.jm.staticcode.constant;

/**
 * 红包接口枚举
 */
public enum RedStatus {

    NO_AUTH {
        public String getName() {
            return "发放失败，此请求可能存在风险，已被微信拦截";
        }
    },
    SENDNUM_LIMIT {
        public String getName() {
            return "该用户今日领取红包个数超过限制";
        }
    },
    CA_ERROR {
        public String getName() {
            return "请求未携带证书，或请求携带的证书出错";
        }
    },
    ILLEGAL_APPID {
        public String getName() {
            return "错误传入了app的appid";
        }
    },
    SIGN_ERROR {
        public String getName() {
            return "商户签名错误";
        }
    },
    FREQ_LIMIT {
        public String getName() {
            return "受频率限制";
        }
    },
    XML_ERROR {
        public String getName() {
            return "请求的xml格式错误，或者post的数据为空";
        }
    },
    PARAM_ERROR {
        public String getName() {
            return "参数错误";
        }
    },
    OPENID_ERROR {
        public String getName() {
            return "Openid错误";
        }
    },
    NOTENOUGH {
        public String getName() {
            return "余额不足";
        }
    },
    FATAL_ERROR {
        public String getName() {
            return "重复请求时，参数与原单不一致";
        }
    },
    SECOND_OVER_LIMITED {
        public String getName() {
            return "企业红包的按分钟发放受限";
        }
    },
    DAY_OVER_LIMITED {
        public String getName() {
            return "企业红包的按天日发放受限";
        }
    },
    MONEY_LIMIT {
        public String getName() {
            return "红包金额发放限制";
        }
    },
    SEND_FAILED {
        public String getName() {
            return "红包发放失败,请更换单号再重试";
        }
    },
    SYSTEMERROR {
        public String getName() {
            return "系统繁忙，请再试。";
        }
    },
    MSGAPPID_ERROR {
        public String getName() {
            return "触达消息给用户appid有误";
        }
    },
    ACCEPTMODE_ERROR {
        public String getName() {
            return "主、子商户号关系校验失败";
        }
    },
    PROCESSING {
        public String getName() {
            return "请求已受理，请稍后使用原单号查询发放结果";
        }
    };

    public abstract String getName();
}
