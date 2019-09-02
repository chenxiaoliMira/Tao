package com.ituotu.tao_core.db;

import java.util.ArrayList;
import java.util.List;

public class Query {
    Class clazz;
    List<QueryCondition> queryConditions;
    List<SortCondition> sortConditions;

    public Query(Class clazz){
        this.clazz = clazz;
    }

    public Query addQueryCondition(QueryCondition queryCondition){
        if (queryConditions == null){
            queryConditions = new ArrayList<QueryCondition>();
        }
        queryConditions.add(queryCondition);
        return this;
    }

    public Query addSortCondition(SortCondition sortCondition){
        if (sortConditions == null){
            sortConditions = new ArrayList<SortCondition>();
        }
        sortConditions.add(sortCondition);
        return this;
    }
}
