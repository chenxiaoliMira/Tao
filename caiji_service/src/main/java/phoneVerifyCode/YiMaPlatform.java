package phoneVerifyCode;

import caiji.db.XueqiuUsers;
import caiji.util.LinkListGrabUtil;
import com.ituotu.tao_core.cache.TaoCache;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.List;

public class YiMaPlatform {

    private String account;
    private String password;
    //spring23230
//    private static String token = "01219938666e5e4eeb1ce23851129436636cf87e8001";
    //spring23231
//    private static String token = "012548107699f7f0cea67b85755db9800405f0812601";
    //spring23230
//    private static String token = "012548165560f9be38635fd2dfaa80148788192b7201";
    //spring23230
//    private static String token = "01254823b55567c407d7b8fce999d5425b5772a7e801";
    //spring23230
    private static String token = "01254825653182fbd5bf9e6fbe9a9b6745c893622001";

    public YiMaPlatform(String account, String password){
        this.account = account;
        this.password = password;
    }

    public String login(){

        String url = "http://api.fxhyd.cn/UserInterface.aspx?action=login&username="+account+"&password="+password;

        try {
            String html = LinkListGrabUtil.grabJsonString(url);
            if (html.contains("success")){
                token = html.substring(8);
                return token;
            }else{
                System.out.println(html);
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getPhoneNumber(String itemId){
        if (token == null){
            this.login();
        }
        String url = String.format("http://api.fxhyd.cn/UserInterface.aspx?action=getmobile&token=%s&itemid=%s",token,itemId);

        try {
            String html = LinkListGrabUtil.grabJsonString(url);
            if (html.contains("success")){
                return html.substring(8);
            }else{
                System.out.println(html);
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getSms(String itemId, String phoneNumer){

        if (token == null){
            this.login();
        }

        String url = String.format("http://api.fxhyd.cn/UserInterface.aspx?action=getsms&token=%s&itemid=%s&mobile=%s", token, itemId, phoneNumer);

        try {
            for (int i=0;i<100;i++){
                String html = LinkListGrabUtil.grabJsonString(url);
                if (html.contains("success")){
                    return html.substring(8);
                }else if(html.contains("3001")){
                    Thread.sleep(3000);
                    continue;
                }else{
                    System.out.println(html);
                    break;
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
