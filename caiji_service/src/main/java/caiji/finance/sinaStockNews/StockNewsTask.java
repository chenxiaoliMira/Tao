package caiji.finance.sinaStockNews;

import caiji.heibaimanhua.duanzi.DuanZiLinkConsumer;
import caiji.heibaimanhua.duanzi.DuanZiLinkProducer;
import com.ituotu.tao_core.cache.TaoCache;

/**
 * 生产者-消费者模式
 * 1、生产者不断生产链接
 * 2、消费者不断获取待采集链接
 * 3、消费者采集内容
 */
public class StockNewsTask {

    public static void start(){
        SinaStockNewsLinkProducer sinaStockNewsLinkProducer = (SinaStockNewsLinkProducer)TaoCache.appContext.getBean("sinaStockNewsLinkProducer");
        sinaStockNewsLinkProducer.start();
        SinaStockNewsLinkConsumer sinaStockNewsLinkConsumer = (SinaStockNewsLinkConsumer)TaoCache.appContext.getBean("sinaStockNewsLinkConsumer");
        sinaStockNewsLinkConsumer.start();
    }
}
