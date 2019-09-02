package com.ituotu.tao_core.db;

import lombok.Getter;
import lombok.Setter;

public class DBField {

    public static final String FIELD_TYPE_INT = "int";
    public static final String FIELD_TYPE_STRING = "string";

    @Getter
    @Setter
    private String fieldName;
    @Getter
    @Setter
    private String fieldType;

    public DBField(String fieldName, String fieldType){
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
