package xueqiu;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XueQiu {

    //https://xueqiu.com/provider/session/token.json?api_path=/statuses/update.json&_=1548257109676

    //post
    //https://xueqiu.com/statuses/update.json
    //请求数据
    //MIME 类型: application/x-www-form-urlencoded; charset=UTF-8
    //status: <p>$康美药业(SH600518)$&nbsp;买了就跌。。</p>
    //session_token: kycuYZ1GFnxhcO0rGPfRZt
    public void postComment(){
        String url = "http://xueqiu.com/provider/session/token.json?api_path=%2Fstatuses%2Fupdate.json&_=1548257109676";
        String postUrl = "https://xueqiu.com/statuses/update.json";
        String cookie = "aliyungf_tc=AQAAAJhUvB/APwQAfN/Ic5vbUDUDn5ix; s=dr1148nzr0; Hm_lvt_1db88642e346389874251b5a1eded6e3=1549872112; _ga=GA1.2.2079081948.1549872112; _gid=GA1.2.441466171.1549872112; device_id=16bd2cd0518ac837da2cae7e252577a1; remember=1; remember.sig=K4F3faYzmVuqC0iXIERCQf55g2Y; xq_a_token=fdb18fd8ebcc7511477ce516578ecf983e24058d; xq_a_token.sig=3fVMoY-JZcTRL48j3RNVDG9oRxA; xqat=fdb18fd8ebcc7511477ce516578ecf983e24058d; xqat.sig=ULuY0muOzasQRvH-ewZowtZF3Hg; xq_r_token=1c7d1fe5e93ca2ed17cfb40da3ecc0eab885fda4; xq_r_token.sig=qF6RN3bAXq0nirPqx7B5EN-4Pxo; xq_is_login=1; xq_is_login.sig=J3LxgPVPUzbBg3Kee_PquUfih7Q; u=5647116760; u.sig=l6KrMgAGO1Q2WTw120yBNejJqg8; bid=ecfb866e3067d46daf6f953348db6612_js020hob; __utma=1.2079081948.1549872112.1549872406.1549872406.1; __utmc=1; __utmz=1.1549872406.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmt=1; __utmb=1.10.10.1549872406; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1549872534";
        try {
            Connection conn = Jsoup.connect(url).timeout(5000);
//            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.header("Accept-Encoding", "gzip, deflate, sdch");
            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            //Cookie是关键
            conn.header("Cookie", cookie);
            conn.header("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.header("Accept-Encoding", "br, gzip, deflate");
            conn.header("X-Requested-With","XMLHttpRequest");
            conn.header("Accept-Encoding","br, gzip, deflate");
            conn.ignoreContentType(true);
            String html = conn.get().text();

            System.out.println(html);
//            String regexp = "{";
            Pattern p2 = Pattern.compile("\\{\"session_token\":\"([\\s\\S]+)\"}");
            Matcher m2 = p2.matcher(html);
            String session_token = "";
            while (m2.find()) {
                session_token = m2.group(1);
                System.out.println(session_token);
            }


            Connection conn2 = Jsoup.connect(postUrl).timeout(5000);
            //Cookie是关键
            conn2.header("Cookie",cookie);
            conn2.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn2.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            conn2.header("Accept", "application/json, text/javascript, */*; q=0.01");
            conn2.header("Accept-Encoding", "br, gzip, deflate");
            conn2.header("X-Requested-With","XMLHttpRequest");
            conn2.header("Accept-Encoding","br, gzip, deflate");

            conn2.data("status","<p>$上证指数(SH000001)$ 2019请开始你的表演</p>");
            conn2.data("session_token",session_token);
            conn2.data("MIME 类型","application/x-www-form-urlencoded; charset=UTF-8");
            String result = conn2.ignoreContentType(true).post().text();
            System.out.println(result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        XueQiu xueQiu = new XueQiu();
        xueQiu.postComment();
    }
}
