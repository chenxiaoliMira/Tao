package caiji.finance.sinaStockNews;

import caiji.db.TargetGrabLink;
import caiji.heibaimanhua.duanzi.SingleDuanziCaijiTask;
import com.ituotu.tao_core.cache.TaoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SinaStockNewsLinkConsumer {

    @Autowired
    @Qualifier("sinaStockNewsTargetLink")
    TargetGrabLink targetGrabLink;

    public void start(){
        TaoCache.mainTaskThreadPool.execute(new Runnable() {
            public void run() {
                while (true){
                    List<TargetGrabLink> targetGrabLinkList = getTargetAddress();
                    for (TargetGrabLink targetGrabLink : targetGrabLinkList){
                        SingleSinaStockNewsCaijiTask singleSinaStockNewsCaijiTask = new SingleSinaStockNewsCaijiTask();
                        singleSinaStockNewsCaijiTask.start(targetGrabLink);
                    }

                    try {
                        Thread.sleep(3600*1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public List<TargetGrabLink> getTargetAddress(){
        return targetGrabLink.findLatestUnGrab100Record();
    }
}
