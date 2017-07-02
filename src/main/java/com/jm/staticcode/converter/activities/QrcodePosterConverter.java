package com.jm.staticcode.converter.activities;

import com.jm.mvc.vo.activities.QrcodePosterCo;
import com.jm.mvc.vo.activities.QrcodePosterForCreateVo;
import com.jm.mvc.vo.activities.QrcodePosterRo;
import com.jm.repository.po.shop.QrcodePoster;

import org.springframework.beans.BeanUtils;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/23
 */
public class QrcodePosterConverter {

    public static QrcodePoster toQrcodePoster(QrcodePosterForCreateVo qrvo) {
    	QrcodePoster qr = new QrcodePoster();
        BeanUtils.copyProperties(qrvo,qr);
        return qr;
    }

    public static QrcodePosterRo p2v(QrcodePoster qrvo) {
        QrcodePosterRo qr = new QrcodePosterRo();
        BeanUtils.copyProperties(qrvo,qr);
        return qr;
    }

    public static QrcodePoster c2p(QrcodePosterCo co) {
        QrcodePoster qr = new QrcodePoster();
        BeanUtils.copyProperties(co,qr);
        return qr;
    }

}
