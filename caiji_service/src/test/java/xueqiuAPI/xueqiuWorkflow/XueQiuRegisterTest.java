package xueqiuAPI.xueqiuWorkflow;

import Constants.HTTPConstants;
import com.ituotu.tao_core.cache.TaoCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import xueqiuAPI.XueQiuAPI;

import static org.junit.Assert.*;

public class XueQiuRegisterTest {

    XueQiuRegister xueQiuRegister = new XueQiuRegister();

    @Before
    public void setUp() throws Exception {
        System.out.println(System.getProperty("user.dir"));
        ApplicationContext appContext = new FileSystemXmlApplicationContext("/src/main/resources/applicationContext.xml");
        TaoCache.appContext = appContext;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void registerAnAccount() {
        String result = xueQiuRegister.registerAnAccount();
        assertEquals(HTTPConstants.SUCCESS, result);
    }

    @Test
    public void registerMultiAccount() {
        try {
            Thread.sleep((3600+600)*1000);
            int registerCount = XueQiuAPI.anonymous_access_tokens.length *2;
            for (int i=0;i<registerCount;i++) {
                String result = xueQiuRegister.registerAnAccount();
                assertEquals(HTTPConstants.SUCCESS, result);
                try {
                    //同一个client_id收验证码间隔60秒
                    //同一个client_id注册间隔1小时
                    //时间间隔不采用客户端传过去的参数，所以需要多个client_id做试验, 一个client_id一次能注册2个账号
//                if (i%2==1){
//                    Thread.sleep((3600+600)*1000);
//                }else{
//                    Thread.sleep(70*1000);
//                }
                    Thread.sleep(70*1000);
                }catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

    }
}