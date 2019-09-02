package com.ituotu.tao_core.db;

import com.ituotu.tao_core.cache.TaoCache;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class BaseDBObject {
    protected DBObject dbObject;
    @Setter
    @Getter
    protected String fieldCommonId;
    @Setter
    @Getter
    protected String fieldParentId;

    @Autowired
    protected DruidDataSource dataSource;

    public BaseDBObject(){
        dbObject = new DBObject(this);
    }

    @PostConstruct
    private void createTable(){
        if (TaoCache.dataSource == null){
            TaoCache.dataSource = dataSource;
        }
        dbObject.createTable();
    }

    public void save(){
        dbObject.save();
    }

    public BaseDBObject findObjectByCommonId(String fieldCommonId){
        String condition = String.format("fieldCommonId='%s'", fieldCommonId);
        List list = dbObject.findTopNResult(1,"fieldCommonId","desc",condition);
        if (list.size()>0){
            return (BaseDBObject) list.get(0);
        }else{
            return null;
        }
    }

    public void updateNonNullValues(){
        dbObject.updateNonNullValues();
    }
}
