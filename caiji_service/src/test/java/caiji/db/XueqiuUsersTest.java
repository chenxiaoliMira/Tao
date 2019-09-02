package caiji.db;

import com.ituotu.tao_core.cache.TaoCache;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XueqiuUsersTest {

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
    public void save(){
        XueqiuUsers xueqiuUser = (XueqiuUsers) TaoCache.appContext.getBean("xueqiuUsers");
        xueqiuUser.setFieldPhoneNumber("15198240026");
        xueqiuUser.setFieldPassword("515570650chenxl");
        xueqiuUser.setFieldNickname("南泥湾的鱼");
        xueqiuUser.setFieldUserState(XueqiuUsers.USER_STATE_NEW);
        xueqiuUser.setFieldCookie_medium("s=dr1148nzr0; _ga=GA1.2.2079081948.1549872112; _gid=GA1.2.441466171.1549872112; device_id=16bd2cd0518ac837da2cae7e252577a1; bid=ecfb866e3067d46daf6f953348db6612_js020hob; __utmz=1.1549872406.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); aliyungf_tc=AQAAALXZb03jzwMArtsKcOO+eE+AoQkM; Hm_lvt_1db88642e346389874251b5a1eded6e3=1549890026,1549934430,1549952026,1549978302; remember=1; remember.sig=K4F3faYzmVuqC0iXIERCQf55g2Y; xq_a_token.sig=PlAnEX7VpjRk0QEyjyUpOHqXu8w; xqat.sig=K6yQnouYfhfBUmd2fvHlcc0_dSc; xq_r_token.sig=EPaMani7AT0cl1NY0a4u13c2lzo; xq_is_login=1; xq_is_login.sig=J3LxgPVPUzbBg3Kee_PquUfih7Q; u=7487960796; u.sig=1PZxtIHNY4NelKAhi2yU7mF9DNc; __utma=1.2079081948.1549872112.1549957660.1549981477.5; __utmc=1; __utmt=1; __utmb=1.3.10.1549981477; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1549981562; xq_a_token=cbe896ad1b97fecc2cedbcebe5f2424411be9b5d; xqat=cbe896ad1b97fecc2cedbcebe5f2424411be9b5d; xq_r_token=de792c863530b836b47dc825708b77e125eb78bd; xq_token_expire=Sat%20Mar%2009%202019%2022%3A26%3A17%20GMT%2B0800%20(CST)");
        xueqiuUser.setFieldMobileSource("51ym.me");
        xueqiuUser.save();

//        xueqiuUser.setFieldPhoneNumber("18783194804");
//        xueqiuUser.setFieldPassword("515570650chenxl");
//        xueqiuUser.setFieldNickname("你好年先生");
//        xueqiuUser.setFieldUserState(XueqiuUsers.USER_STATE_NEW);
//        xueqiuUser.setFieldCookie_medium("_ga=GA1.2.994412879.1549960784; _gid=GA1.2.702580254.1549960784; aliyungf_tc=AQAAABiIiSaNEQ8ADAheLzpqaF+cHFok; Hm_lvt_1db88642e346389874251b5a1eded6e3=1549960410,1549961024; device_id=1b9644c07549ae4a7c755f462528269c; remember=1; remember.sig=K4F3faYzmVuqC0iXIERCQf55g2Y; xq_a_token.sig=0CDJAnKpbNhOD8GLm2tnUHT_OAw; xqat.sig=CMp66FENBAGnczN2f4LLJ3a8sUY; xq_r_token.sig=Dsi_Vot5zZzHTSqGj9uvvsAwNoA; xq_is_login=1; xq_is_login.sig=J3LxgPVPUzbBg3Kee_PquUfih7Q; u=2099581042; u.sig=kemYHUovvfb6YTsbCVaYcFB0Y7A; s=ey15mwpz5h; bid=ffe038d4559ad8f7e75fd375309905e6_js1ium4u; __utma=1.994412879.1549960784.1549961152.1549961152.1; __utmc=1; __utmz=1.1549961152.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmt=1; xq_a_token=a8d8f970d85cdf3742c54958d9ea0d07e3c1f7e7; xqat=a8d8f970d85cdf3742c54958d9ea0d07e3c1f7e7; xq_r_token=83cac3bdd7cd962c520b0470d7a3db5037447abd; xq_token_expire=Sat%20Mar%2009%202019%2016%3A47%3A42%20GMT%2B0800%20(CST); __utmb=1.4.10.1549961152; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1549961263");
//        xueqiuUser.setFieldMobileSource("51ym.me");
//        xueqiuUser.save();
    }

    @Test
    public void renewLoginState(){
        String url = "http://xueqiu.com/provider/session/token.json?api_path=%2Fstatuses%2Fupdate.json&_=1548257109676";
        String postUrl = "https://xueqiu.com/statuses/update.json";
        String cookie = "";

        XueqiuUsers xueqiuUser = (XueqiuUsers) TaoCache.appContext.getBean("xueqiuUsers");
        List<XueqiuUsers> xueqiuUsersList = xueqiuUser.findLatestNRecord(10000);

        for (XueqiuUsers temp : xueqiuUsersList){
            try {
                Connection conn = Jsoup.connect(url).timeout(5000);
                conn.header("Accept-Language", "zh-CN,zh;q=0.8");
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                //Cookie是关键
                conn.header("Cookie", temp.getFieldCookie_medium());
                conn.header("Accept", "application/json, text/javascript, */*; q=0.01");
                conn.header("Accept-Encoding", "br, gzip, deflate");
                conn.header("X-Requested-With","XMLHttpRequest");
                conn.header("Accept-Encoding","br, gzip, deflate");
                conn.ignoreContentType(true);
                String html = conn.get().text();

                System.out.println(html);
                Pattern p2 = Pattern.compile("\\{\"session_token\":\"([\\s\\S]+)\"}");
                Matcher m2 = p2.matcher(html);
                boolean success = m2.find();
                if (!success){
                    System.out.println(temp.getFieldPhoneNumber());
                }
                Assert.assertTrue(success);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}