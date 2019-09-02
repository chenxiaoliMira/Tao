package caiji.heibaimanhua.duanzi;

import com.ituotu.tao_core.cache.TaoCache;

/**
 * 生产者-消费者模式
 * 1、生产者不断生产链接
 * 2、消费者不断获取待采集链接
 * 3、消费者采集内容
 */
public class DuanZiTask {

    public static void start(){
        DuanZiLinkProducer duanZiLinkProducer = (DuanZiLinkProducer)TaoCache.appContext.getBean("duanZiLinkProducer");
        duanZiLinkProducer.start();
        DuanZiLinkConsumer duanZiLinkConsumer = (DuanZiLinkConsumer)TaoCache.appContext.getBean("duanZiLinkConsumer");
        duanZiLinkConsumer.start();
    }
}
