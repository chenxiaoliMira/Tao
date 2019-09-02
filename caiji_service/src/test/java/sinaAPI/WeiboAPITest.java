package sinaAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeiboAPITest {

    WeiboAPI weiboAPI = new WeiboAPI();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void callSendSms(){
        weiboAPI.callSendSms("13735469739");
    }

    @Test
    public void callLoginAPI() {
        //to do
        weiboAPI.callSendSms("13735469739");

        weiboAPI.callLoginAPI("13735469739", "090434");
    }

    @Test
    public void callSendWeiboAPI(){
        weiboAPI.callSendWeiboAPI("激光玩具不要给小孩子玩，易伤眼睛");
    }
}