package sinaAPI;

import Constants.HTTPConstants;
import caiji.util.HTTPUtil;
import caiji.util.MD5Util;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.URLEncoder;
import java.util.Date;

public class WeiboAPI {

    public static final String HOST = "https://api.weibo.cn";

    private Connection setConHeader(Connection conn){
        conn.header("Accept-Language", "zh-CN,zh;q=0.8");
        conn.header("User-Agent", "Weibo/32804 (iPhone; iOS 12.1.2; Scale/2.00)");
        conn.header("Accept", "application/json");
        conn.header("Accept-Encoding", "br, gzip, deflate");
        conn.header("Host","api.xueqiu.com");
        return conn;
    }

    public String callSendSms(String phoneNumber){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/2/account/login_sendcode?wm=3333_2001&uid=1012274310759&i=8d2617a&sensors_is_first_day=true&from=1092093010&sensors_device_id=FFDB1ACD-0E24-417E-AC30-E6BDBE064A8B&c=iphone&v_p=71&skin=default&v_f=1&networktype=wifi&checktoken=ca3698ea927ae3f1311985f7f058c2a2&did=d9cf264eebde8e8f03adbf0ee476186f&lang=zh_CN&ua=iPhone6,2__weibo__9.2.0__iphone__os12.1.2&sflag=1&ft=0&b=0&cum=0D5A19BD",HOST);

            Connection conn = Jsoup
                    .connect(api)
                    .timeout(10000);
            conn = setConHeader(conn);

            conn.cookie("x-sessionid","D3D3C2A7-A9AA-4FB0-92A4-0CEBFF7C6A8F");
            conn.cookie("u","1676172359");

            // 新增部分
//            conn.setHostnameVerifier(new SslHandshakeExc_NsanMatchingIp().new TrustAnyHostnameVerifier());
            conn.validateTLSCertificates(false);

            conn.maxBodySize(0);
            conn.ignoreContentType(true);


            String postData = String.format("moduleID=account&luicode=10000229&lfid=0&phone=%s&uicode=10000760",phoneNumber);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            return HTTPConstants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String callLoginAPI(String phoneNumber, String sms_code){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/2/account/login?wm=3333_2001&uid=1012274310759&i=8d2617a&sensors_is_first_day=true&from=1092093010&sensors_device_id=FFDB1ACD-0E24-417E-AC30-E6BDBE064A8B&c=iphone&v_p=71&skin=default&v_f=1&networktype=wifi&checktoken=ca3698ea927ae3f1311985f7f058c2a2&did=d9cf264eebde8e8f03adbf0ee476186f&lang=zh_CN&ua=iPhone6,2__weibo__9.2.0__iphone__os12.1.2&sflag=1&ft=0&b=0&cum=401C8750",HOST);

            Connection conn = Jsoup
                    .connect(api)
                    .timeout(10000);
            conn = setConHeader(conn);

            conn.cookie("x-sessionid","D3D3C2A7-A9AA-4FB0-92A4-0CEBFF7C6A8F");
            conn.cookie("u","1676172359");

            // 新增部分
//            conn.setHostnameVerifier(new SslHandshakeExc_NsanMatchingIp().new TrustAnyHostnameVerifier());
            conn.validateTLSCertificates(false);

            conn.maxBodySize(0);
            conn.ignoreContentType(true);


            String postData = String.format("firstLogin=1&phone=%s&luicode=10000760&featurecode=10000225&uicode=10000062&guestid=1012274310759&cfrom=&imei=d71d1ab5fb89a608ce9d052487967e531caa22a0&device_name=iPhone&smscode=%s&device_id=d9cf264eebde8e8f03adbf0ee476186f9b9b05b6&getcookie=1&lfid=0&moduleID=account&aid=01AmyPDzsXmxyr4QGg_2m01chCf4IAsPRYpZrXS0v0BJy2Ti0.",phoneNumber, sms_code);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            return HTTPConstants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String callSendWeiboAPI(String content){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/2/statuses/send?gsid=_2A25xbiMqDeRxGeNI61EZ9SvNzT2IHXVQOjHirDV6PUJbkdAKLRDwkWpNSHzG2lbnVV1zRR58pp4BueiB5GNcV8Lx&wm=3333_2001&i=8d2617a&sensors_is_first_day=true&from=1092093010&sensors_device_id=FFDB1ACD-0E24-417E-AC30-E6BDBE064A8B&c=iphone&v_p=71&skin=default&s=d62b9999&v_f=1&networktype=wifi&b=0&lang=zh_CN&ua=iPhone6,2__weibo__9.2.0__iphone__os12.1.2&sflag=1&ft=0&cum=5F67AAB3",HOST);

            Connection conn = Jsoup
                    .connect(api)
                    .timeout(10000);
            conn = setConHeader(conn);

            conn.cookie("x-sessionid","D3D3C2A7-A9AA-4FB0-92A4-0CEBFF7C6A8F");
            conn.cookie("u","1676172359");

            // 新增部分
//            conn.setHostnameVerifier(new SslHandshakeExc_NsanMatchingIp().new TrustAnyHostnameVerifier());
            conn.validateTLSCertificates(false);

            conn.maxBodySize(0);
            conn.ignoreContentType(true);

//            content = URLEncoder.encode(content,"UTF-8");
            String postData = String.format("user_input=1&phone_id=&check_id=1550472151387&moduleID=composer&luicode=10000155&featurecode=10000001&uicode=10000017&client_mblogid=iPhone-C699D98E-8D64-44E3-BE85-2CE1AD2B020E&source_text=&ext=effectname:|network:wifi&retry=0&act=add&rcontent=%s&lfid=100015603855161&is_vip_paid=0&content=%s", content, content);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            return HTTPConstants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }
}
