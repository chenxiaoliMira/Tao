package caiji.stock.bkList;

import com.ituotu.tao_core.cache.TaoCache;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import static org.junit.Assert.*;

public class CaijiBKListTest {

    @Test
    public void start() {
        ApplicationContext appContext = new FileSystemXmlApplicationContext("/src/main/resources/applicationContext.xml");
        TaoCache.appContext = appContext;

        CaijiBKList caijiBKList = (CaijiBKList)appContext.getBean("caijiBKList");
        caijiBKList.start();
    }
}