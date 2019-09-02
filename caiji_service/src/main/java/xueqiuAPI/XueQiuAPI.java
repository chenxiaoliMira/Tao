package xueqiuAPI;

import Constants.HTTPConstants;
import caiji.db.Article;
import caiji.db.Stocks;
import caiji.db.XueqiuUsers;
import caiji.util.ArticleUtil;
import caiji.util.HTTPUtil;
import caiji.util.MD5Util;
import caiji.util.x;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import sun.security.provider.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class XueQiuAPI {
    public static final String LOGIN_HOST = "https://api.xueqiu.com";

    public static final String NanGuo_AccessToken = "5e0f196437aeb838f88080061dbed4a486a98021";
    //匿名用户token有效时间是1天？
    public static String anonymous_access_tokens[] = {
            "5e0f196437aeb838f88080061dbed4a486a98021",
            "7227d6459600e7c3b09f21f8ca2cb3972dfab83f",
            "8be5d4b2e7bd4ec68347dd026219474039c3e403",
            "f5ad0d4de6cdb8b658dfa9b6707290a184ab4615",
            "41d8bab107004dfe2af21597a2a4744c15bc1ae9",
            "b38348b19bc020d768a69b854bee5bcee5acf487",
            "fd8214e8792730b89152463b3db55f8c2a806091",
            "5e695c6c8e0f4a9da22b10454b42f7ef1161cd2c",
            "cc482a2011a27504d9a6d7834360d0cc83cdfe57",
            "ed38d017370c63d9a36317ca56edf29376b6b6ee",






//            "5e0f196437aeb838f88080061dbed4a486a98021",
//            "c9b595a24ec0cf3b9fea7e0ce3fdde65b2077434",

//            "fb98d10cfe4ba8bc7d6678d61d5859ce449c4b7a",
//            "667f58b128f1eac7d2c2fa5e29f77c4cc0c7a3be",
            };

    private Connection getConn(String api){
        Connection conn = Jsoup
            .connect(api)
            .timeout(10000);
        conn.header("Accept-Language", "zh-CN,zh;q=0.8");
        conn.header("User-Agent", "Xueqiu iPhone 11.16");
        conn.header("Accept", "application/json");
        conn.header("Accept-Encoding", "br, gzip, deflate");
        conn.header("Host","api.xueqiu.com");
        conn.ignoreHttpErrors(true);
        conn.validateTLSCertificates(false);
        conn.maxBodySize(0);
        conn.ignoreContentType(true);
        return conn;
    }

    public JSONObject callShikeCreate(String deviceUUID){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/stat/shike/create.json?idfa=%s", LOGIN_HOST, deviceUUID);
            Connection conn = getConn(api);

            String html = conn.get().text();
            System.out.println(html);

            JSONObject jsonObject = JSONObject.fromObject(html);

            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取验证码前，需要先获取token, 最终目的是获取匿名用户access_token,用其他登录用户的token是否可以
     *
     * post中 sign的算法未知，只知道sign与timestamp, deviceuuid有关，所以目前只传固定的sign,timestamp,sid=deviceuuid
     * 方案：把sign,timestamp, uuid收集起来，收集到多组数据，获取多组access_token
     *
     *
     * 数据1：sign=de5773512c7b9843729aa66dd98eb83f17d3f1b9&timestamp=1550157420000  deviceUUID:669B864A-87B1-4941-A17D-174E13736FD8    ====>   access_token:5e0f196437aeb838f88080061dbed4a486a98021
     * 数据2：device_uuid=40465724-B791-4750-9348-F69A6ECF9C4A&sign=1f5119c2657e362515d34c0f70302e7b1773bdd4&timestamp=1550640921000    ====>   access_token:c9b595a24ec0cf3b9fea7e0ce3fdde65b2077434
     *
     * 13735469739 access_token: 87fb97b7ecec8688dcf59a5794aeb5279161ada1 是否可用来注册？
     *
     * 不用传sign?
     *
     *
     *
     *
     * 得到的sign是否可以给别的deviceuuid去获取难码？待验证
     * 同一个UUID在获取的是同一个access_token? 2-14 2-20 iphone 5s获取到的是同一个access_token
     *
     * @param deviceUUID
     * @return
     */
    public JSONObject callGetTokenBeforeSendSms(String deviceUUID, long signTimestamp, String sign){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/provider/oauth/token?_=%d&x=0.98&_s=85fc48&_t=%s.1676172359.%d.%d",LOGIN_HOST,timestamp,deviceUUID,timestamp-450000,timestamp+2);
            Connection conn = getConn(api);

            String postData = String.format("client_id=WiCimxpj5H&client_secret=TM69Da3uPkFzIdxpTEm6hp&device_uuid=%s&grant_type=password&is_register=0&sid=%s&sign=%s&timestamp=%d&type=2&version=11.15.2",deviceUUID,deviceUUID,sign,signTimestamp);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);

            JSONObject jsonObject = JSONObject.fromObject(html);

            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用发短信API
     *
     * 可能报错："error_description": "请求过于频繁, 请稍后再试",
     * 通过修改timestamp是否有效，经测试，无效，每次间隔应大于60秒
     *
     * @param phoneNumber
     * @return
     */
    public String callSendSMSAPI(String phoneNumber, String uuid,String accessToken){
        try {
            long timestamp = new Date().getTime();
            String api = String.format("%s/account/sms/send_verification_code.json?_=%d&x=0.265&_s=b6840a&_t=%s.8610954243.%d.%d", LOGIN_HOST,timestamp,uuid,timestamp-450000,timestamp+2);

            Connection conn = getConn(api);

//            conn.cookie("xq_a_token","61569a72464f39bed7d0d4a180ab0eaac1dd3f75");
//            conn.cookie("xq_a_token","3f89046d29bc2654c29d5a047be6365f85e533e6");
            conn.cookie("xq_a_token",accessToken);
            conn.cookie("u","1676172359");

            conn.data("areacode", "86");
            conn.data("sid", uuid);
            conn.data("telephone", phoneNumber);
            conn.data("voice", "0");

            String html = conn.post().text();
            System.out.println(html);
            if (html.contains("true")){
                return HTTPConstants.SUCCESS;
            }else{
                return HTTPConstants.FAIL;
            }
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String callLoginAPI(String phoneNumber, String password, String deviceUUID, String access_token, String userId){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/provider/oauth/token?_=%d&x=0.98&_s=85fc48&_t=%s.%s.%d.%d",LOGIN_HOST,timestamp,deviceUUID,userId, timestamp-450000,timestamp+2);

            Connection conn = getConn(api);

            conn.cookie("xq_a_token",access_token);
            conn.cookie("u",userId);

            password = MD5Util.MD5(password);
            String postData = String.format("areacode=86&captcha=&client_id=WiCimxpj5H&client_secret=TM69Da3uPkFzIdxpTEm6hp&device_uuid=%s&grant_type=password&is_register=0&password=%s&sid=%s&telephone=%s",deviceUUID,password, deviceUUID, phoneNumber);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            return HTTPConstants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    /**
     * 调用手机验证码登录接口，通过isRegister区分是注册还是登录
     *
     * 通过client_id, client_secret进行注册限制？还是通过device_uuid进行限制？待验证，应该是access_token, 因为device_uuid是随机生成的, client_id是固定的
     * 是否可用用户名及密码直接注册？ ==> 已测试过，不行
     * 已存在的用户，不能进行再次注册，is_register第一次传0，第二次传1， 已注册的用户再次传1会发生什么情况？
     *
     * 可能有请求太频繁的报错, 能不能修改timestamp来避开该规则？经测试，不行
     *
     * 注册成功后获得token并保存，后面设置昵称和密码要用
     *
     * @param phoneNumber
     * @param verifyCode
     * @param deviceUUID
     * @return 注册成功返回json数据
     */
    public String callRegisterAPI(String phoneNumber, String verifyCode, String deviceUUID, String accessToken, long userid){
        try {
//            //两个注册请求间隔为4小时
//            if (deltaHour<364*24) {
//                deltaHour += 4;
//            }
//            long timestamp = new Date().getTime()-3600*1000 * (365*24 - deltaHour);

            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/provider/oauth/token?_=%d&x=0.98&_s=85fc48&_t=%s.%d.%d.%d",LOGIN_HOST,timestamp,deviceUUID,userid, timestamp-450000,timestamp+1);

            Connection conn = getConn(api);

            conn.cookie("xq_a_token",accessToken);
            conn.cookie("u",userid + "");

            String postData = String.format("areacode=86&client_id=WiCimxpj5H&client_secret=TM69Da3uPkFzIdxpTEm6hp&code=%s&device_uuid=%s&grant_type=telephone_auto_login&is_register=1&sid=%s&telephone=%s",verifyCode, deviceUUID, deviceUUID, phoneNumber);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            return html;
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    /**
     * 昵称可用，返回: HTTPConstants.SUCCESS
     * 昵称不可用，返回: HTTPConstants.FAIL
     * @param nickname
     * @param deviceUUID
     * @return
     */
    public String callCheckNicknameAPI(String nickname, String deviceUUID, String access_token){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
//            https://101.201.175.228/account/check_nickname.json?nickname=%E5%86%8D%E8%A7%81qinga&_=1550125993812&x=0.62&_s=5ff63f&_t=669B864A-87B1-4941-A17D-174E13736FD8.1676172359.1550125918019.1550125993814
            String api = String.format("%s/account/check_nickname.json?nickname=%s&_=%d&x=0.98&_s=85fc48&_t=%s.1676172359.%d.%d", LOGIN_HOST,URLEncoder.encode(nickname,"UTF-8"),timestamp,deviceUUID,timestamp-450000,timestamp+2);

            Connection conn = getConn(api);

            conn.cookie("xq_a_token",access_token);
            conn.cookie("u","1676172359");

            String html = conn.get().text();
            System.out.println(html);
            if (html.contains("false")){
                return HTTPConstants.SUCCESS;
            }else{
                return HTTPConstants.FAIL;
            }
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String callSetUsernameAPI(String userId, String nickName, String deviceUUID, String access_token){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);

            //https://182.92.251.113/user/setting/profile.json?_=1550135389269&x=0.2185&_s=cc982b&_t=669B864A-87B1-4941-A17D-174E13736FD8.1676172359.1550135382414.1550135389270
            String api = String.format("%s/user/setting/profile.json?_=%d&x=0.98&_s=85fc48&_t=%s.1676172359.%d.%d", LOGIN_HOST,timestamp,deviceUUID,timestamp-450000,timestamp+2);

            Connection conn = getConn(api);

            conn.cookie("xq_a_token",access_token);
            conn.cookie("u",userId);

//            conn.data("screenName", URLEncoder.encode(nickName,"UTF-8"));
            conn.data("screenName", nickName);

            String html = conn.post().text();
            System.out.println(html);
            //服务端返回：{"success":true}
            if (html.contains("true")){
                return HTTPConstants.SUCCESS;
            }else{
                return HTTPConstants.FAIL;
            }
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    /**
     * 修改密码会导致access_token发生变化，需要重新获取token, 获取新token时要传入refresh_token
     * @param userId
     * @param oldPassword
     * @param deviceUUID
     * @return
     */
    public String callSetPasswordAPI(String userId, String oldPassword, String newPassword, String deviceUUID, String access_token){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);

            String api = String.format("%s/account/change_password.json?_=%d&x=0.98&_s=85fc48&_t=%s.1676172359.%d.%d", LOGIN_HOST,timestamp,deviceUUID,timestamp-450000,timestamp+2);

            Connection conn = getConn(api);

            conn.cookie("xq_a_token",access_token);
            conn.cookie("u",userId);

//            conn.data("screenName", URLEncoder.encode(nickName,"UTF-8"));
            newPassword = MD5Util.MD5(newPassword);
            if (oldPassword!=null && oldPassword.length()>0) {
                oldPassword = MD5Util.MD5(oldPassword);
            }else{
                oldPassword = "";
            }
            String postData = String.format("oldpasswd=%s&passwd1=%s&passwd2=%s", oldPassword,newPassword,newPassword);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            //服务端返回：{"success":true}
            if (html.contains("true")){
                return HTTPConstants.SUCCESS;
            }else{
                return HTTPConstants.FAIL;
            }
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String callSetAvatarAPI(String phoneNumber, String verifyCode, String deviceUUID){
        return HTTPConstants.SUCCESS;
    }


    /**
     * 测试直接用手机号及密码注册
     * grant_type：password
     * is_register: 1
     *
     * 服务端返回：
     * http code: 400
     * error_description": "用户名或密码错误",
     *
     * @param phoneNumber
     * @param password
     * @param deviceUUID
     * @return
     */
    public String callRegisterUsePasswordAPI(String phoneNumber, String password, String deviceUUID, String access_token){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);
            String api = String.format("%s/provider/oauth/token?_=%d&x=0.98&_s=85fc48&_t=%s.1676172359.%d.%d",LOGIN_HOST,timestamp,deviceUUID,timestamp-450000,timestamp+2);
            Connection conn = getConn(api);

            conn.cookie("xq_a_token",access_token);
            conn.cookie("u","1676172359");

            password = MD5Util.MD5(password);
            String postData = String.format("areacode=86&client_id=WiCimxpj5H&client_secret=TM69Da3uPkFzIdxpTEm6hp&device_uuid=%s&grant_type=password&is_register=1&sid=%s&telephone=%s&password=%s", deviceUUID, deviceUUID, phoneNumber, password);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            return HTTPConstants.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String callPostToStock(XueqiuUsers xueqiuUser, Article article, Stocks stock){
        try {
            long timestamp = new Date().getTime();
            System.out.println(timestamp);

            //https://182.92.251.113/user/setting/profile.json?_=1550135389269&x=0.2185&_s=cc982b&_t=669B864A-87B1-4941-A17D-174E13736FD8.1676172359.1550135382414.1550135389270
            String api = String.format("%s/statuses/update.json?_=%d&x=0.98&_s=85fc48&_t=%s.%s.%d.%d", LOGIN_HOST,timestamp,xueqiuUser.getFieldDeviceUUID(),xueqiuUser.getFieldUserId(), timestamp-450000,timestamp+2);
            Connection conn = getConn(api);

            conn.cookie("xq_a_token",xueqiuUser.getFieldAccessToken());
            conn.cookie("u",xueqiuUser.getFieldUserId());

            String postContent = ArticleUtil.getPostContentToXueQiu(article, stock);
            String postData = String.format("card_param=%s&card_type=stock&is_private=0&original=0&right=0&status=%s",stock.getFieldStockCode(),postContent);
            conn.data(HTTPUtil.Split(postData));

            String html = conn.post().text();
            System.out.println(html);
            if (html.contains("\"is_private\":false") && html.contains("\"canEdit\":true")){
                return HTTPConstants.SUCCESS;
            }else{
                return HTTPConstants.FAIL;
            }
        }catch (Exception e){
            e.printStackTrace();
            return HTTPConstants.FAIL;
        }
    }

    public String generatToken(String uuid, long timestamp){
        String signResult = x.b(uuid + x.b("xq_anonymous_since_2014") + timestamp);
        System.out.println("generated sign: "+signResult);
        return signResult;
    }
}
