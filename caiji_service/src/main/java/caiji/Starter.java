package caiji;

import caiji.finance.FinanceTask;
import com.ituotu.tao_core.cache.TaoCache;
import caiji.heibaimanhua.HeiBaiManHuaTask;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 1、采集黑白漫画
 * 2、采集新浪财经
 *       新浪财经频道
 *       新浪股票博客
 */
public class Starter {
    public static void main(String[] args){

        ApplicationContext appContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        TaoCache.appContext = appContext;

        HeiBaiManHuaTask.start();
        FinanceTask.start();
    }
}
