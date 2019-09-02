package phoneVerifyCode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xueqiuAPI.XueQiuAPI;

import java.util.UUID;

import static org.junit.Assert.*;

public class YiMaPlatformTest {

    YiMaPlatform yiMaPlatform;

    @Before
    public void setUp() throws Exception {
        yiMaPlatform = new YiMaPlatform("spring23230", "515570650chenxl");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void login() {
        String result = yiMaPlatform.login();
        assertNotNull(result);
    }

    @Test
    public void getSms(){
        String itemId = "1232";

        //获取号码
        String phoneNumber = yiMaPlatform.getPhoneNumber(itemId);
        if (phoneNumber == null){
            fail();
            return;
        }

        String uuid = UUID.randomUUID().toString();
        //访问雪球发短信链接
        XueQiuAPI xueQiuAPI = new XueQiuAPI();

        //accessToken未传，该test会fail
        xueQiuAPI.callSendSMSAPI(phoneNumber, uuid, "");

        //获取短信
//        String phoneNumber = "18845842914";
        String result = yiMaPlatform.getSms(itemId, phoneNumber);
        assertNotNull(result);
    }
}