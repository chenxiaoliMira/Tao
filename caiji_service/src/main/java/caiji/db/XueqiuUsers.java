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
public class XueqiuUsers extends BaseDBObject {

    public static final int USER_STATE_NEW = 1;
    public static final int USER_STATE_OLD = 10;

    @Getter
    @Setter
    private String fieldPhoneNumber;
    @Getter
    @Setter
    private String fieldUserAccount;
    @Getter
    @Setter
    private String fieldPassword;
    @Getter
    @Setter
    private String fieldNickname;
    @Getter
    @Setter
    private int fieldUserState;
    @Getter
    @Setter
    private String fieldCookie_medium;
    @Getter
    @Setter
    private String fieldMobileSource;
    @Getter
    @Setter
    private String fieldRemark;
    @Getter
    @Setter
    private String fieldDeviceUUID;
    @Getter
    @Setter
    private String fieldClientID;
    @Getter
    @Setter
    private String fieldClientSecret;
    @Getter
    @Setter
    private String fieldUserId;
    @Getter
    @Setter
    private String fieldClientType;
    @Getter
    @Setter
    private String fieldAccessToken;
    //创建时间
    @Setter
    @Getter
    private Date fieldCreateTime;

    /**
     * 获取最近添加的N条记录
     * @param n
     * @return
     */
    public List<XueqiuUsers> findLatestNRecord(int n){
        String condition = "fieldAccessToken is not null";
        return dbObject.findTopNResult("*", n,"fieldCommonId","desc",condition);
    }
//
//    public List<XueqiuPostRecord> findLatestUnGrab100Record(){
//        String condition = String.format("fieldTargetSite='%s' and fieldTargetModule='%s' and fieldGrabState=0", fieldTargetSite,fieldTargetModule);
//        return dbObject.findTopNResult(100,"fieldCreateTime","desc",condition);
//    }

}
