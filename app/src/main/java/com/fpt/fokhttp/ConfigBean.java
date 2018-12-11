package com.fpt.fokhttp;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/07/20 21:02
 *   desc    : 配置信息实体
 *   version : 1.0
 * </pre>
 */
public class ConfigBean {

    private String weiXinAppsercet;
    private String fastDfsUrl;
    private String serviceTel;
    private String listVisits;
    private List<InquiringpriceBean> inquiringprice;
    /* 医生药方提成比例范围*/
    private String doctorPrescription;
    /*关于我们的微信号*/
    private String weChat;

    public String getDoctorPrescription() {
        return doctorPrescription == null ? "0" : doctorPrescription;
    }

    public String getWeChat() {
        return weChat == null ? "无" : weChat;
    }

    public String getWeiXinAppSecret() {
        return weiXinAppsercet == null ? "" : weiXinAppsercet;
    }

    public String getFastDfsUrl() {
        return fastDfsUrl == null ? "" : fastDfsUrl;
    }

    public String getServiceTel() {
        return serviceTel == null ? "" : serviceTel;
    }

    public String getListVisits() {
        return listVisits == null ? "" : listVisits;
    }

    public List<InquiringpriceBean> getInquiringPrice() {
        if (inquiringprice == null) {
            return new ArrayList<>();
        }
        return inquiringprice;
    }

    public static class InquiringpriceBean {
        /**
         * itemcode : 1
         * itemname : 10
         */

        private String itemcode;
        private String itemname;

        public String getItemCode() {
            return itemcode == null ? "" : itemcode;
        }

        public String getItemName() {
            return itemname == null ? "" : itemname;
        }

    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "weiXinAppsercet='" + weiXinAppsercet + '\'' +
                ", fastDfsUrl='" + fastDfsUrl + '\'' +
                ", serviceTel='" + serviceTel + '\'' +
                ", listVisits='" + listVisits + '\'' +
                ", inquiringprice=" + inquiringprice +
                ", doctorPrescription='" + doctorPrescription + '\'' +
                ", weChat='" + weChat + '\'' +
                '}';
    }
}
