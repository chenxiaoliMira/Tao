package caiji.heibaimanhua.duanzi;

import com.ituotu.tao_core.cache.TaoCache;
import caiji.db.TargetGrabLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DuanZiLinkConsumer {

    @Autowired
    @Qualifier("duanziTargetLink")
    TargetGrabLink targetGrabLink;

    public void start(){
        TaoCache.mainTaskThreadPool.execute(new Runnable() {
            public void run() {
                while (true){
                    List<TargetGrabLink> targetGrabLinkList = getDuanziTargetAddress();
                    for (TargetGrabLink targetGrabLink : targetGrabLinkList){
                        SingleDuanziCaijiTask singleDuanziCaijiTask = new SingleDuanziCaijiTask();
                        singleDuanziCaijiTask.start(targetGrabLink);
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

    public List<TargetGrabLink> getDuanziTargetAddress(){
        return targetGrabLink.findLatestUnGrab100Record();
    }
}
