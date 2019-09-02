package xueqiuAPI.xueqiuWorkflow;

import com.ituotu.tao_core.cache.TaoCache;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import static org.junit.Assert.*;

public class XueQiuPostArticleTest {

    XueQiuPostArticle xueQiuPostArticle = new XueQiuPostArticle();

    @Before
    public void setUp() throws Exception {
        ApplicationContext appContext = new FileSystemXmlApplicationContext("/src/main/resources/applicationContext.xml");
        TaoCache.appContext = appContext;
    }

    @Test
    public void postAnArticle() {
    }

    @Test
    public void getRandomXueQiuUser() {
        xueQiuPostArticle.getRandomXueQiuUser();
    }

    @Test
    public void getStockByArticle() {
    }
}