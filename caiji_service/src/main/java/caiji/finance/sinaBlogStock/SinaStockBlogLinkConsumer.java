package caiji.finance.sinaBlogStock;

import caiji.db.TargetGrabLink;
import com.ituotu.tao_core.cache.TaoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SinaStockBlogLinkConsumer {

    @Autowired
    @Qualifier("sinaStockBlogTargetLink")
    TargetGrabLink targetGrabLink;

    public void start(){
        TaoCache.mainTaskThreadPool.execute(new Runnable() {
            public void run() {
                while (true){
                    List<TargetGrabLink> targetGrabLinkList = getTargetAddress();
                    for (TargetGrabLink targetGrabLink : targetGrabLinkList){
                        SingleSinaStockBlogCaijiTask singleSinaStockBlogCaijiTask = new SingleSinaStockBlogCaijiTask();
                        singleSinaStockBlogCaijiTask.start(targetGrabLink);
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
