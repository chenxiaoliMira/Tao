package caiji.util;

import caiji.db.Article;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 列表链接抓取工具
 */
public class LinkListGrabUtil {
    /**
     * 抓取网页，并将重复链接过滤
     * @return
     */
    public static ArrayList<String> grabHTMLAndRemoveRepeatRecord(String listUrl, String regexp){
        ArrayList<String> urlList = new ArrayList<String>();
        try {
            String html = LinkListGrabUtil.grabHTML(listUrl);
//            String html = Jsoup
//                    .connect(listUrl)
//                    .timeout(10000).userAgent("Mozilla").get().html();

            String s = html;
            Pattern p2 = Pattern
                    .compile(regexp);
            Matcher m2 = p2.matcher(s);

            while (m2.find()) {
                urlList.add(m2.group());
            }
            urlList = ListUtil.removeRepeatRecord(urlList);

        }catch (Exception e){
            e.printStackTrace();
        }
        return urlList;
    }

    public static String grabHTML(String url){
        try {
            Connection conn = Jsoup
                    .connect(url)
                    .timeout(10000);
            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            conn.header("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.header("Accept-Encoding", "br, gzip, deflate");
            conn.header("X-Requested-With","XMLHttpRequest");
            conn.ignoreHttpErrors(true);
            conn.validateTLSCertificates(false);
            conn.maxBodySize(0);
            conn.ignoreContentType(true);
            String html = conn.get().html();
            return html;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String grabJsonString(String url){
        try {
            Connection conn = Jsoup
                    .connect(url)
                    .timeout(20000);
            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            conn.header("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.header("Accept-Encoding", "br, gzip, deflate");
            conn.header("X-Requested-With","XMLHttpRequest");
            conn.ignoreHttpErrors(true);
            conn.validateTLSCertificates(false);
            conn.maxBodySize(0);
            conn.ignoreContentType(true);
            String html = conn.get().text();
            return html;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
