package caiji.db;

import com.ituotu.tao_core.db.BaseDBObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 雪球推送记录
 */
@Repository
@Scope("prototype")
public class Stocks extends BaseDBObject {

    //沪股
    public static final int STOCK_TYPE_SH = 1;
    //深股
    public static final int STOCK_TYPE_SZ = 2;
    //创业板
    public static final int STOCK_TYPE_CY = 3;

    @Getter
    @Setter
    private String fieldStockName;
    @Getter
    @Setter
    private String fieldStockCode;
    @Getter
    @Setter
    private int fieldStockType;
    @Getter
    @Setter
    private String fieldBKCodes;
    @Getter
    @Setter
    private String fieldBKNames;

    /**
     * 获取最近添加的N条记录
     * @param n
     * @return
     */
    public List<Stocks> findLatestNRecord(int n){
        return dbObject.findTopNResult(n,"fieldCommonId","desc",null);
    }

//    public List<XueqiuPostRecord> findLatestUnGrab100Record(){
//        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s' and fieldGrabState=0", fieldTargetSite,fieldTargetModule);
//        return dbObject.findTopNResult(100,"fieldCreateTime","desc",condition);
//    }

}
