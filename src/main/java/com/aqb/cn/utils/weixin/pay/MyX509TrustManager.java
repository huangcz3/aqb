package com.aqb.cn.utils.weixin.pay;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 证书信任管理器（用于https请求）
 * @author 鏉庢妗�
 * @date 2014-11-21涓嬪崍9:15:08
 */
public class MyX509TrustManager implements X509TrustManager {

    // 委托到默认信任管理器
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    // 委托到默认信任管理器
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    // 杩斿洖鍙椾俊浠荤殑X509璇佷功鏁扮粍
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
