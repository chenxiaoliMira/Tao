package caiji.db;

import com.ituotu.tao_core.db.BaseDBObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 待采集的链接
 */
@Repository
@Scope("prototype")
public class TargetGrabLink extends BaseDBObject {

    public static final int GRAB_STATE_UNGRAB = 0;
    public static final int GRAB_STATE_GRABBED = 1;

    @Getter
    @Setter
    private String fieldTargetLink;
    @Getter
    @Setter
    private int fieldGrabState;
    @Getter
    @Setter
    private Date fieldCreateTime;
    @Getter
    @Setter
    private String fieldTargetSite;
    @Getter
    @Setter
    private String fieldTargetModule;

    /**
     * 获取最近添加的N条记录
     * @param n
     * @return
     */
    public List<TargetGrabLink> findLatestNRecord(int n){
        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s'", fieldTargetSite,fieldTargetModule);
        return dbObject.findTopNResult(n,"fieldCreateTime","desc",condition);
    }

    public List<TargetGrabLink> findLatestUnGrab100Record(){
        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s' and fieldGrabState=0", fieldTargetSite,fieldTargetModule);
        return dbObject.findTopNResult(100,"fieldCreateTime","desc",condition);
    }

}
