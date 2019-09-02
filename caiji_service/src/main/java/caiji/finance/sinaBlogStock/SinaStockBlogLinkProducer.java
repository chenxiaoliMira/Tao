package caiji.finance.sinaBlogStock;

import caiji.db.TargetGrabLink;
import caiji.util.LinkListGrabUtil;
import com.ituotu.tao_core.cache.TaoCache;
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
public class SinaStockBlogLinkProducer {

    //新浪博客 -》 财经-大盘走势
    private static final String URL = "http://finance.sina.com.cn/roll/index.d.html?cid=57568&page=1";

    private static final String FILTER_PATTERN = "http://blog.sina.com.cn/s/blog[\\S]+tj=fina";

    @Autowired
    @Qualifier("sinaStockBlogTargetLink")
    TargetGrabLink targetGrabLink;

    public void start(){
        TaoCache.mainTaskThreadPool.execute(new Runnable() {
            public void run() {
                while (true){
                    //10分钟采集一次
                    runAGrab();
                    try {
                        Thread.sleep(10*60*1000);
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
            //通过spring创建，自动注入dataSource
            TargetGrabLink targetGrabLink = (TargetGrabLink)TaoCache.appContext.getBean("sinaStockBlogTargetLink");
            targetGrabLink.setFieldTargetLink(url);
            targetGrabLink.setFieldGrabState(TargetGrabLink.GRAB_STATE_UNGRAB);
            targetGrabLink.setFieldCreateTime(date);
            targetGrabLink.save();
        }

    }


}
