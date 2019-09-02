package caiji.heibaimanhua.duanzi;

import com.ituotu.tao_core.cache.TaoCache;
import caiji.db.TargetGrabLink;
import caiji.util.LinkListGrabUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 生产者
 * 每隔1小时抓取一次待采集链接
 * 作为单例模式，交由spring容器管理
 */
@Controller
public class DuanZiLinkProducer {

    private static final String URL = "http://heibaimanhua.com/duanzishou";

    //"http://heibaimanhua.com/duanzishou/152451.html";
    private static final String FILTER_PATTERN = "http://heibaimanhua.com/duanzishou/[0-9]+.html";

    @Autowired
    @Qualifier("duanziTargetLink")
    TargetGrabLink targetGrabLink;

    public void start(){
        TaoCache.mainTaskThreadPool.execute(new Runnable() {
            public void run() {
                while (true){
                    runAGrab();
                    try {
                        Thread.sleep(3600*1000);
                    }catch (Exception e){
                    }
                }
            }
        });

    }

    public void runAGrab(){

        ArrayList<String> urlList = LinkListGrabUtil.grabHTMLAndRemoveRepeatRecord(URL,FILTER_PATTERN);

        List arrayList = targetGrabLink.findLatestNRecord(500);

        Iterator<TargetGrabLink> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            TargetGrabLink targetGrabLink = iterator.next();
            if(urlList.contains(targetGrabLink.getFieldTargetLink())) {
                urlList.remove(targetGrabLink.getFieldTargetLink());
            }
        }

        Date date = new Date();
        for (String url : urlList){
            TargetGrabLink targetGrabLink = (TargetGrabLink)TaoCache.appContext.getBean("duanziTargetLink");
            targetGrabLink.setFieldTargetLink(url);
            targetGrabLink.setFieldGrabState(TargetGrabLink.GRAB_STATE_UNGRAB);
            targetGrabLink.setFieldCreateTime(date);
            targetGrabLink.save();
        }

    }


}
