package caiji.db;

import com.ituotu.tao_core.db.BaseDBObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 雪球推送记录
 */
@Repository
@Scope("prototype")
public class XueqiuPostRecord extends BaseDBObject {

    @Getter
    @Setter
    private String fieldUserId;
    @Getter
    @Setter
    private String fieldArticleId;
    @Getter
    @Setter
    private Date fieldPostTime;
    @Getter
    @Setter
    private String fieldStockId;

//    /**
//     * 获取最近添加的N条记录
//     * @param n
//     * @return
//     */
//    public List<XueqiuPostRecord> findLatestNRecord(int n){
//        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s'", fieldTargetSite,fieldTargetModule);
//        return dbObject.findTopNResult(n,"fieldCreateTime","desc",condition);
//    }
//
//    public List<XueqiuPostRecord> findLatestUnGrab100Record(){
//        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s' and fieldGrabState=0", fieldTargetSite,fieldTargetModule);
//        return dbObject.findTopNResult(100,"fieldCreateTime","desc",condition);
//    }

}
