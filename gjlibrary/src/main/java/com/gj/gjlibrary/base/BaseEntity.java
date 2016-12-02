package com.gj.gjlibrary.base;

import java.io.Serializable;

/**
 * Created by guojing on 2016/2/17.
 * 基础实体类
 */
public class BaseEntity<T> implements Serializable {
    public boolean success;
    public int error_code;
    public String error;
    public String url;
    public T data;
}
