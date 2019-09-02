package com.ituotu.tao_core.db;

import com.ituotu.tao_core.cache.TaoCache;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ituotu.tao_core.constants.DateFormatConstants;
import com.ituotu.tao_core.db.cache.DBCache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBObject {
    private static final String TABLE_PREFIX = "t_";
    private String tableName;
    private LinkedList<DBField> fieldList;
    private Object mappedObject;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatConstants.DB_DATE_FORMAT);

    public DBObject(Object obj){
        this.mappedObject = obj;
        tableName = TABLE_PREFIX + obj.getClass().getSimpleName();
        addFields();
    }

    public List findTopNResult(int n, String sortField, String sortType,String condition){
        return findTopNResult("*", n, sortField, sortType, condition);
    }

    /**
     *
     * @param sections 需要查哪些字段
     * @param n
     * @param sortField
     * @param sortType
     * @param condition
     * @return
     */
    public List findTopNResult(String sections, int n, String sortField, String sortType,String condition){
        return findTopNResult(sections,n,sortField,sortType,condition,0);
    }

    /**
     *
     * @param sections 需要查哪些字段
     * @param n
     * @param sortField
     * @param sortType
     * @param condition
     * @return
     */
    public List findTopNResult(String sections, int n, String sortField, String sortType,String condition, int fromIndex){
        String sql = "";
        fromIndex = fromIndex>=0?fromIndex:0;
        if (condition == null){
            sql = String.format("select %s from %s order by %s %s limit %d,%d",sections, tableName, sortField, sortType,fromIndex, n);
        }else{
            sql = String.format("select %s from %s where %s order by %s %s limit %d,%d",sections, tableName, condition, sortField, sortType,fromIndex, n);
        }
        return runQuerySQL(sql);
    }

    public void save(){
        StringBuilder sql = new StringBuilder(String.format("insert into %s set ", tableName));

        Class<?> clazz = mappedObject.getClass();
        for (DBField field : fieldList){


            String methodName = "getF"+field.getFieldName().substring(1);
            try {
                Method setFunc = clazz.getMethod(methodName);
                Object object = setFunc.invoke(mappedObject);
                if (object==null){
                    continue;
                }
                if (field.getFieldType().equals("varchar(200)") || field.getFieldType().equals("MEDIUMTEXT") || field.getFieldType().equals("LONGTEXT")){
                    sql.append(String.format("%s = '%s',",field.getFieldName(),object.toString()));
                }
                if (field.getFieldType().equals("int")){
                    sql.append(String.format("%s = '%d',",field.getFieldName(),Integer.parseInt(object.toString())));
                }
                if (field.getFieldType().equals("double")){
                    sql.append(String.format("%s = '%f',",field.getFieldName(),Double.parseDouble(object.toString())));
                }
                if (field.getFieldType().equals("timestamp")){
                    sql.append(String.format("%s = '%s',",field.getFieldName(), simpleDateFormat.format(object)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        sql.append(String.format("fieldCommonId = '%s'", UUID.randomUUID().toString()));
        runSQL(sql.toString());
    }

    public void updateNonNullValues(){
        StringBuilder sql = new StringBuilder(String.format("update %s set ", tableName));
        try {
            Class<?> clazz = mappedObject.getClass();
            for (DBField field : fieldList){


                String methodName = "getF"+field.getFieldName().substring(1);

                    Method setFunc = clazz.getMethod(methodName);
                    Object object = setFunc.invoke(mappedObject);
                    if (object==null){
                        continue;
                    }
                    if (field.getFieldType().equals("varchar(200)") || field.getFieldType().equals("MEDIUMTEXT") || field.getFieldType().equals("LONGTEXT")){
                        sql.append(String.format("%s = '%s',",field.getFieldName(),object.toString()));
                    }
                    if (field.getFieldType().equals("int")){
                        sql.append(String.format("%s = '%d',",field.getFieldName(),Integer.parseInt(object.toString())));
                    }
                    if (field.getFieldType().equals("double")){
                        sql.append(String.format("%s = '%f',",field.getFieldName(),Double.parseDouble(object.toString())));
                    }
                    if (field.getFieldType().equals("timestamp")){
                        sql.append(String.format("%s = '%s',",field.getFieldName(), simpleDateFormat.format(object)));
                    }

            }
            sql.deleteCharAt(sql.length()-1);
            Method setFunc = clazz.getMethod("getFieldCommonId");
            Object object = setFunc.invoke(mappedObject);
            sql.append(String.format(" where fieldCommonId='%s'", object.toString()));
            runSQL(sql.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addFields() {
        fieldList = new LinkedList<DBField>();

        //我们项目的所有实体类都继承BaseDomain （所有实体基类：该类只是串行化一下）
        //不需要的自己去掉即可

        // 拿到该类
        Class<?> clz = mappedObject.getClass();
        while (clz != null){
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields) {// --for() begin

                if (field.getName().startsWith("field")) {
                    // 如果类型是String
                    if (field.getGenericType().toString().equals(
                            "class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        if (field.getName().endsWith("_medium")){
                            fieldList.add(new DBField(field.getName(),"MEDIUMTEXT"));
                        }else if (field.getName().endsWith("_long")){
                            fieldList.add(new DBField(field.getName(),"LONGTEXT"));
                        }else {
                            fieldList.add(new DBField(field.getName(),"varchar(200)"));
                        }

                        continue;
                    }

                    // 如果类型是Integer
                    if (field.getGenericType().toString().equals(
                            "class java.lang.Integer")) {
                        fieldList.add(new DBField(field.getName(),"int"));
                        continue;
                    }

                    // 如果类型是Double
                    if (field.getGenericType().toString().equals(
                            "class java.lang.Double")) {
                        fieldList.add(new DBField(field.getName(),"double"));
                        continue;

                    }

                    // 如果类型是Boolean 是封装类
                    if (field.getGenericType().toString().equals(
                            "class java.lang.Boolean")) {
                        fieldList.add(new DBField(field.getName(),"int"));
                        continue;
                    }

                    // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
                    // 反射找不到getter的具体名
                    if (field.getGenericType().toString().equals("boolean")) {
                        fieldList.add(new DBField(field.getName(),"int"));
                        continue;
                    }
                    // 如果类型是Date
                    if (field.getGenericType().toString().equals(
                            "class java.util.Date")) {
                        fieldList.add(new DBField(field.getName(),"timestamp"));
                        continue;
                    }
                    // 如果类型是Short
                    if (field.getGenericType().toString().equals(
                            "class java.lang.Short")) {
                        fieldList.add(new DBField(field.getName(),"int"));
                        continue;
                    }
                    if (field.getGenericType().toString().equals("int")) {
                        fieldList.add(new DBField(field.getName(),"int"));
                        continue;
                    }
                    // 如果还需要其他的类型请自己做扩展
                }

            }
            clz = clz.getSuperclass();
        }
    }

    private List handleResultSetForList(ResultSet rs){
        try {
            Class clazz = mappedObject.getClass();
            ArrayList arrayList = new ArrayList();
            while(rs.next()){
                Object object = clazz.newInstance();
                for (DBField dbField : fieldList){
                    String methodName = "setF"+dbField.getFieldName().substring(1);
                    if (!isExistColumn(rs,dbField.getFieldName())){
                        continue;
                    }
                    if (dbField.getFieldType().equals("varchar(200)")){
                        Method setFunc = clazz.getMethod(methodName, String.class);
                        setFunc.invoke(object, rs.getString(dbField.getFieldName()));
                    }else if (dbField.getFieldType().equals("MEDIUMTEXT")){
                        Method setFunc = clazz.getMethod(methodName, String.class);
                        setFunc.invoke(object, rs.getString(dbField.getFieldName()));
                    }else if (dbField.getFieldType().equals("LONGTEXT")){
                        Method setFunc = clazz.getMethod(methodName, String.class);
                        setFunc.invoke(object, rs.getString(dbField.getFieldName()));
                    }else if (dbField.getFieldType().equals("int")){
                        Method setFunc = clazz.getMethod(methodName, int.class);
                        setFunc.invoke(object, rs.getInt(dbField.getFieldName()));
                    }else if (dbField.getFieldType().equals("double")){
                        Method setFunc = clazz.getMethod(methodName, double.class);
                        setFunc.invoke(object, rs.getDouble(dbField.getFieldName()));
                    }else if (dbField.getFieldType().equals("timestamp")){
                        Method setFunc = clazz.getMethod(methodName, Date.class);
                        setFunc.invoke(object, rs.getDate(dbField.getFieldName()));
                    }
                }
                arrayList.add(object);
            }
            return arrayList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断查询结果集中是否存在某列
     * @param rs 查询结果集
     * @param columnName 列名
     * @return true 存在; false 不存咋
     */
    private boolean isExistColumn(ResultSet rs, String columnName) {
        try {
            if (rs.findColumn(columnName) > 0 ) {
                return true;
            }
        }
        catch (SQLException e) {
            return false;
        }

        return false;
    }


    public void createTable(){
        if (DBCache.dbTables.containsKey(tableName)){
            return;
        }
        DBCache.dbTables.put(tableName,"created");
        // mysql
        String sql = String.format("SELECT table_name FROM information_schema.TABLES WHERE table_name ='%s'",tableName);
        try{
            DruidPooledConnection con = TaoCache.dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.first()){
                String createSql = String.format("create table %s (", tableName);
                StringBuilder stringBuilder = new StringBuilder(createSql);
                for (DBField dbField : fieldList){
                    if (dbField.getFieldName().equals("fieldCommonId")) {
                        stringBuilder.append("fieldCommonId varchar(36) PRIMARY KEY,");
                    }else if (dbField.getFieldName().equals("fieldParentId")) {
                        stringBuilder.append("fieldParentId varchar(36),");
                    }else{
                        stringBuilder.append(String.format("%s %s,", dbField.getFieldName(), dbField.getFieldType()));
                    }
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                stringBuilder.append(")");
                runSQL(stringBuilder.toString());
            }

            ps.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private List runQuerySQL(String sql){
        try{
            DruidPooledConnection con = TaoCache.dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            List list = handleResultSetForList(resultSet);

            ps.close();
            con.close();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean runSQL(String sql){
        try{
            DruidPooledConnection con = TaoCache.dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            boolean isSuccess = ps.execute();
            ps.close();
            con.close();
            return isSuccess;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
