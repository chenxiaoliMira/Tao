package caiji.finance.sinaBlogStock;

import caiji.db.Article;
import caiji.db.TargetGrabLink;
import com.ituotu.tao_core.cache.TaoCache;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import static org.junit.Assert.*;

public class SingleSinaStockBlogCaijiTaskTest {

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
    public void grabArticle() {

        SingleSinaStockBlogCaijiTask task = new SingleSinaStockBlogCaijiTask();
        TargetGrabLink link = new TargetGrabLink();
        link.setFieldTargetLink("http://blog.sina.com.cn/s/blog_5485ea430102z54b.html?tj=fina");
        link.setFieldCommonId("dc0b59a9-1b7a-40db-8eec-d18d55574c3f");
        Article article = task.grabArticle(link);

        Assert.assertTrue(article.getFieldContent_medium().trim().length()>50);
    }
}