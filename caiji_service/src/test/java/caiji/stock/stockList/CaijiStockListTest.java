package caiji.stock.stockList;

import com.ituotu.tao_core.cache.TaoCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CaijiStockListTest {

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
    public void start() {
        CaijiStockList caijiStockList = (CaijiStockList) TaoCache.appContext.getBean("caijiStockList");
        caijiStockList.start();
    }
}