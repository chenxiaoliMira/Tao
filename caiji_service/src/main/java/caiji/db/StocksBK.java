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
public class StocksBK extends BaseDBObject {

    //行业板块
    public static final int STOCK_BK_TYPE_HY = 1;
    //概念板块
    public static final int STOCK_BK_TYPE_GN = 2;

    @Getter
    @Setter
    private String fieldStockBKName;
    //东方财富通的板块代码
    @Getter
    @Setter
    private String fieldStockBKCode;
    //板块类型：行业板块、概念板块
    @Getter
    @Setter
    private int fieldStockBKType;

    /**
     * 获取最近添加的N条记录
     * @param n
     * @return
     */
    public List<StocksBK> findLatestAllRecords(int n){
        return dbObject.findTopNResult(n,"fieldCommonId","desc",null);
    }

//    public List<XueqiuPostRecord> findLatestUnGrab100Record(){
//        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s' and fieldGrabState=0", fieldTargetSite,fieldTargetModule);
//        return dbObject.findTopNResult(100,"fieldCreateTime","desc",condition);
//    }

}
