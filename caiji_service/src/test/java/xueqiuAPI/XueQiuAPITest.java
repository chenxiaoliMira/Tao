package xueqiuAPI;

import Constants.HTTPConstants;
import caiji.db.Article;
import caiji.db.Stocks;
import caiji.db.XueqiuUsers;
import caiji.util.x;
import com.ituotu.tao_core.cache.TaoCache;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.UUID;

import static org.junit.Assert.*;

public class XueQiuAPITest {

    XueQiuAPI xueQiuAPI;

    @Before
    public void setUp() throws Exception {
        xueQiuAPI = new XueQiuAPI();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void callGetTokenBeforeSendSms(){
        //iphone 5s
        //数据1：sign=de5773512c7b9843729aa66dd98eb83f17d3f1b9&timestamp=1550157420000  deviceUUID:669B864A-87B1-4941-A17D-174E13736FD8    ====>   access_token:5e0f196437aeb838f88080061dbed4a486a98021
        String uuid = "669B864A-87B1-4941-A17D-174E13736FD8";
        long timestamp = 1550157420000l;
        String sign = "de5773512c7b9843729aa66dd98eb83f17d3f1b9";
        JSONObject jsonObject = xueQiuAPI.callGetTokenBeforeSendSms(uuid,timestamp,sign);
//        assertTrue(result.length() == 40);
    }

    @Test
    public void callSendSMSAPI() {
        String uuid = UUID.randomUUID().toString();

        //数据1：sign=de5773512c7b9843729aa66dd98eb83f17d3f1b9&timestamp=1550157420000  deviceUUID:669B864A-87B1-4941-A17D-174E13736FD8    ====>   access_token:5e0f196437aeb838f88080061dbed4a486a98021
        String uuid0 = "669B864A-87B1-4941-A17D-174E13736FD8";
        long timestamp = 1550157420000l;
        String sign = "de5773512c7b9843729aa66dd98eb83f17d3f1b9";
        JSONObject jsonObject = xueQiuAPI.callGetTokenBeforeSendSms(uuid0,timestamp,sign);
        String result = jsonObject.getString("access_token");

        assertTrue(result.length() == 40);
        result = xueQiuAPI.callSendSMSAPI("13735469739", uuid,result);
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callRegisterAPI(){
        xueQiuAPI.callRegisterAPI("17009433242","7313","776be8c5-7dff-45c6-8e9f-460dee5990e9","3f89046d29bc2654c29d5a047be6365f85e533e6", 1);
    }

    @Test
    public void callLoginAPI(){
//        String uuid = "ddca28fd-aa33-4412-bf8d-d8277a04f132";
//        String result = xueQiuAPI.callLoginAPI("18845853042", "01kGHV37lyK", uuid, "bb73b14c688ce35510a3f4d21ec57ae1f0622f78");
//        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callLoginAPIWithPhone(){
        ApplicationContext appContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        TaoCache.appContext = appContext;
        XueqiuUsers xueqiuUsers2 = (XueqiuUsers)appContext.getBean("xueqiuUsers");
        XueqiuUsers xueqiuUsers = (XueqiuUsers) xueqiuUsers2.findObjectByCommonId("c758d2ff-a5c2-41c0-bc42-35610582c3bb");

        String uuid = xueqiuUsers.getFieldDeviceUUID();
        String result = xueQiuAPI.callLoginAPI(xueqiuUsers.getFieldPhoneNumber(), xueqiuUsers.getFieldPassword(), uuid, xueqiuUsers.getFieldAccessToken(), xueqiuUsers.getFieldUserId());
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callRegisterUsePasswordAPI(){
        String uuid = UUID.randomUUID().toString();
        String result = xueQiuAPI.callRegisterUsePasswordAPI("18899998888", "515570650chenxl", uuid, XueQiuAPI.NanGuo_AccessToken);
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callCheckNicknameAPI(){
        String uuid = "669B864A-87B1-4941-A17D-174E13736FD8";
        String result = xueQiuAPI.callCheckNicknameAPI("再见",uuid, XueQiuAPI.NanGuo_AccessToken);
        assertEquals(HTTPConstants.FAIL, result);
        result = xueQiuAPI.callCheckNicknameAPI("再见青春的记忆",uuid, XueQiuAPI.NanGuo_AccessToken);
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callSetUsernameAPI(){
        String uuid = "669B864A-87B1-4941-A17D-174E13736FD8";
        String result = xueQiuAPI.callSetUsernameAPI("1676172359","再见青春的记",uuid, XueQiuAPI.NanGuo_AccessToken);
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callSetPasswordAPI(){
        String uuid = "669B864A-87B1-4941-A17D-174E13736FD8";
        String result = xueQiuAPI.callSetPasswordAPI("1676172359","515570650chenxl", "5155706chenxl",uuid, XueQiuAPI.NanGuo_AccessToken);
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void callPostToStock(){

//        XueqiuUsers xueqiuUser = new XueqiuUsers();
//        xueqiuUser.setFieldAccessToken("edb6dc4b7fe2cf3f148d9ee85ebf73eac35ceb29");
//        //userid不重要，主要是根据access_token来关联用户的，相当于session
//        xueqiuUser.setFieldUserId("6492561021");
////        xueqiuUser.setFieldUserId("8856538725");
//        xueqiuUser.setFieldDeviceUUID("669B864A-87B1-4941-A17D-174E13736FD8");

        ApplicationContext appContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        TaoCache.appContext = appContext;
        XueqiuUsers xueqiuUsers2 = (XueqiuUsers)appContext.getBean("xueqiuUsers");
        XueqiuUsers xueqiuUser = (XueqiuUsers) xueqiuUsers2.findObjectByCommonId("3cb64f73-316e-4856-baf7-4c13a6a37c99");

        Article article = new Article();
        article.setFieldContent_medium("已经放飞自己了");

        Stocks stock = new Stocks();
        stock.setFieldStockCode("SZ000725");
        stock.setFieldStockName("京东方A");

        String result = xueQiuAPI.callPostToStock(xueqiuUser,article,stock);
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void testFail(){
        //fail之后不会再继续执行
//        assertTrue(false);
        System.out.println("aa");
        assertTrue(false);
        System.out.println("bb");
        assertTrue(false);
        System.out.println("cc");
    }

    @Test
    public void testOthers(){
        for (int i=6;i>-1;i--) {
            String uuid = "669B864A-87B1-4941-A17D-174E13736FD"+i;
            long timestamp = 1550157420000l;
            String sign = "de5773512c7b9843729aa66dd98eb83f17d3f1b9";
            String signResult = x.b(uuid + x.b("xq_anonymous_since_2014") + timestamp);
            System.out.println("\""+signResult+"\",");
        }
    }
}