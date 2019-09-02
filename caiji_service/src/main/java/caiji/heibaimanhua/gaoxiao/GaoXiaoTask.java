package caiji.heibaimanhua.gaoxiao;

import caiji.heibaimanhua.duanzi.DuanZiLinkProducer;
import caiji.heibaimanhua.duanzi.SingleDuanziCaijiTask;

import java.util.LinkedList;

/**
 * 生产者-消费者模式
 * 1、生产者不断生产链接
 * 2、消费者不断获取待采集链接
 * 3、消费者采集内容
 */
public class GaoXiaoTask {

    public static void start(){
        GaoxiaoLinkProducer.start();
        while (true){
            LinkedList<String> targetAddresses = getGaoxiaoTargetAddress();
            for (String targetAddress : targetAddresses){
                SingleGaoxiaoCaijiTask singleGaoxiaoCaijiTask = new SingleGaoxiaoCaijiTask();
                singleGaoxiaoCaijiTask.start(targetAddress);
            }
        }
    }


    public static LinkedList<String> getGaoxiaoTargetAddress(){
        return null;
    }
}
