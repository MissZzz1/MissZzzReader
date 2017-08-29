package com.zhao.myreader.entity;

import java.io.Serializable;

/**
 * Created by zhao on 2016/11/2.
 */

public class Custom implements Serializable {

    private static final long serialVersionUID = 5088810102696918656L;

    private String id;
    private String type;//类型

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
