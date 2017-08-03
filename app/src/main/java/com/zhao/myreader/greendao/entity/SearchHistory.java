package com.zhao.myreader.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * 搜索记录
 * Created by zhao on 2017/8/3.
 */

@Entity
public class SearchHistory implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String content;//内容

    @NotNull
    private String createDate;//创建时间

    @Generated(hash = 1175489714)
    public SearchHistory(String id, @NotNull String content,
                         @NotNull String createDate) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
