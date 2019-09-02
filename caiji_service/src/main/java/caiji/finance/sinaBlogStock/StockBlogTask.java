package caiji.finance.sinaBlogStock;

import com.ituotu.tao_core.cache.TaoCache;

/**
 * 生产者-消费者模式
 * 1、生产者不断生产链接
 * 2、消费者不断获取待采集链接
 * 3、消费者采集内容
 */
public class StockBlogTask {

    public static void start(){
        SinaStockBlogLinkProducer sinaStockNewsLinkProducer = (SinaStockBlogLinkProducer)TaoCache.appContext.getBean("sinaStockBlogLinkProducer");
        sinaStockNewsLinkProducer.start();
        SinaStockBlogLinkConsumer sinaStockNewsLinkConsumer = (SinaStockBlogLinkConsumer)TaoCache.appContext.getBean("sinaStockBlogLinkConsumer");
        sinaStockNewsLinkConsumer.start();
    }
}
