package com.zhao.myreader.greendao.entity;



import androidx.annotation.Nullable;

import com.zhao.myreader.greendao.service.BookService;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 章节
 * Created by zhao on 2017/7/24.
 */

@Entity
public class Chapter {
    @Id
    private String id;

    private String bookId;//章节所属书的ID
    private int number;//章节序号
    private String title;//章节标题
    private String url;//章节链接
    @Nullable
    private String content;//章节正文


    @Generated(hash = 1019441369)
    public Chapter(String id, String bookId, int number, String title, String url,
                   String content) {
        this.id = id;
        this.bookId = bookId;
        this.number = number;
        this.title = title;
        this.url = url;
        this.content = content;
    }
    @Generated(hash = 393170288)
    public Chapter() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }





  

}
