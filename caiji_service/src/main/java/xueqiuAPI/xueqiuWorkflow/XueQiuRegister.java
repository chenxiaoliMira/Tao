package xueqiuAPI.xueqiuWorkflow;

import Constants.HTTPConstants;
import caiji.db.XueqiuUsers;
import caiji.util.HTTPUtil;
import caiji.util.StringRandom;
import com.ituotu.tao_core.cache.TaoCache;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nicknameLib.NicknameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import phoneVerifyCode.YiMaPlatform;
import xueqiuAPI.XueQiuAPI;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XueQiuRegister {

    public String registerAnAccount(){
        YiMaPlatform yiMaPlatform = new YiMaPlatform("spring23234", "515570650chenxl");
        String itemId = "1232";
        String deviceUUID = UUID.randomUUID().toString().toUpperCase();
        XueQiuAPI xueQiuAPI = new XueQiuAPI();

        xueQiuAPI.callShikeCreate(deviceUUID);

        //获取未登录状态下的access_token
//        String access_token = xueQiuAPI.callGetTokenBeforeSendSms("669B864A-87B1-4941-A17D-174E13736FD8");
        long timeStamp = new Date().getTime();
        String sign = xueQiuAPI.generatToken(deviceUUID, timeStamp);

        JSONObject jsonObject = xueQiuAPI.callGetTokenBeforeSendSms(deviceUUID,timeStamp, sign);
        String access_token = jsonObject.getString("access_token");
        long userId = jsonObject.getLong("uid");
        if (access_token.length() != 40){
            return HTTPConstants.FAIL;
        }
        System.out.println("step1: 获取未登录状态下的access_token成功 ===》 "+access_token);

        //获取号码
        String phoneNumber = yiMaPlatform.getPhoneNumber(itemId);
        if (phoneNumber == null){
            return HTTPConstants.FAIL;
        }
        System.out.println("step2: 获取号码成功 ===》 "+phoneNumber);

        //访问雪球发短信链接
        //请求过于频繁的问题需解决，每次间隔应大于60秒
        String result = xueQiuAPI.callSendSMSAPI(phoneNumber, deviceUUID,access_token);
        if (result.equalsIgnoreCase(HTTPConstants.FAIL)){
            return HTTPConstants.FAIL;
        }
        System.out.println("step3: 雪球发短信接口调用成功 随机deviceUUID: "+ deviceUUID);

        //获取短信
        result = yiMaPlatform.getSms(itemId, phoneNumber);
        String verifyCode = null;
        //解析短信
        if (result != null){
            Pattern p2 = Pattern
                    .compile("验证码[\\s\\S]+([0-9]{4})[\\s\\S]+请在5分钟");
            Matcher m2 = p2.matcher(result);
            if (m2.find()) {
                verifyCode = m2.group(1);
            }
        }
        if (verifyCode == null){
            return HTTPConstants.FAIL_ON_YM;
        }
        System.out.println("step4: 易码获取验证码成功 ===》 "+verifyCode);

        //访问雪球登录链接，is_register传1表示注册
        //如果该账号已注册呢？待验证
        /**
         * 可能有请求太频繁的报错, 能不能修改timestamp来避开该规则？经测试，不行
         */
        result = xueQiuAPI.callRegisterAPI(phoneNumber, verifyCode, deviceUUID, access_token, userId);
        if (result.equalsIgnoreCase(HTTPConstants.FAIL) || result.contains("\"error_code\":\"")){
            try {
                //有时候发现报错，再试一次又能注册成功
                Thread.sleep(10*1000);
                result = xueQiuAPI.callRegisterAPI(phoneNumber, verifyCode, deviceUUID, access_token, userId);
                if (result.equalsIgnoreCase(HTTPConstants.FAIL) || result.contains("\"error_code\":\"")){
                    return HTTPConstants.FAIL;
                }
            }catch (Exception e){
                e.printStackTrace();
                return HTTPConstants.FAIL;
            }

        }
        System.out.println("step5: 雪球注册成功");

        //注册后获取access_token和uid
        jsonObject = JSONObject.fromObject(result);
        access_token = jsonObject.getString("access_token");
        long userid = jsonObject.getLong("uid");
        if (access_token == null || access_token.length() == 0){
            return HTTPConstants.FAIL;
        }
        System.out.println("step6: 雪球注册成功，新accessToken ===》 "+access_token);

        //设置昵称，从昵称库获取昵称，并确保未被占用
        String nickname = null;
        while (true) {
            nickname = NicknameUtil.randomANickName();
            if (xueQiuAPI.callCheckNicknameAPI(nickname,deviceUUID, access_token).equalsIgnoreCase(HTTPConstants.SUCCESS)){
                break;
            }
        }
        System.out.println("step7: 从昵称库获取昵称成功 ===》 "+nickname);

        //修改昵称
        result = xueQiuAPI.callSetUsernameAPI(userid+"",nickname,deviceUUID, access_token);
        if (result.equalsIgnoreCase(HTTPConstants.FAIL)){
            return HTTPConstants.FAIL;
        }
        System.out.println("step8: 雪球修改昵称成功");

        //修改密码
        //生成随机密码
        String password = StringRandom.getStringRandom(11);
        result = xueQiuAPI.callSetPasswordAPI("1676172359","", password,deviceUUID, access_token);
        if (result.equalsIgnoreCase(HTTPConstants.FAIL)){
            return HTTPConstants.FAIL;
        }
        System.out.println("step9: 雪球修改密码成功 ===》 "+password);

        //把账号保存起来
        XueqiuUsers xueqiuUser = (XueqiuUsers) TaoCache.appContext.getBean("xueqiuUsers");
        xueqiuUser.setFieldPhoneNumber(phoneNumber);
        xueqiuUser.setFieldPassword(password);
        xueqiuUser.setFieldNickname(nickname);
        xueqiuUser.setFieldUserState(XueqiuUsers.USER_STATE_NEW);
        xueqiuUser.setFieldMobileSource("51ym.me");

        xueqiuUser.setFieldClientID("WiCimxpj5H");
        xueqiuUser.setFieldClientSecret("TM69Da3uPkFzIdxpTEm6hp");
        xueqiuUser.setFieldUserId(userid+"");
        xueqiuUser.setFieldDeviceUUID(deviceUUID);
        xueqiuUser.setFieldAccessToken(access_token);
        xueqiuUser.setFieldCreateTime(new Date());
        xueqiuUser.save();
        System.out.println("step10: 雪球账号保存成功 ===》 "+phoneNumber);

        return HTTPConstants.SUCCESS;
    }

    public static void main(String []args){
        System.out.println(System.getProperty("user.dir"));
//        ApplicationContext appContext = new FileSystemXmlApplicationContext("/applicationContext.xml");
        ApplicationContext appContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        TaoCache.appContext = appContext;

//        System.out.println("success");
//        System.out.println(NicknameUtil.randomANickName());

//        XueQiuAPI xueQiuAPI = new XueQiuAPI();
//        String uuid = "669B864A-87B1-4941-A17D-174E13736FD8";
//        String result = xueQiuAPI.callCheckNicknameAPI("再见",uuid, XueQiuAPI.NanGuo_AccessToken);
//        System.out.println(result);
//        result = xueQiuAPI.callCheckNicknameAPI("再见青春的记忆",uuid, XueQiuAPI.NanGuo_AccessToken);
//        System.out.println(result);


        XueQiuRegister xueQiuRegister = new XueQiuRegister();
        try {
//            Thread.sleep((3600+600)*1000);
            int registerCount = 1000 ;
            for (int i=0;i<registerCount;i++) {
                String result = xueQiuRegister.registerAnAccount();
                if (result.equalsIgnoreCase(HTTPConstants.FAIL)){
                    break;
                }
                try {
                    //同一个client_id收验证码间隔60秒
                    //同一个client_id注册间隔1小时
                    //时间间隔不采用客户端传过去的参数，所以需要多个client_id做试验, 一个client_id一次能注册2个账号
                if (i%2==1){
                    Thread.sleep((40*60)*1000);
                }else{
                    Thread.sleep(70*1000);
                }
                }catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
