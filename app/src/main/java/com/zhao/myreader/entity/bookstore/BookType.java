package com.zhao.myreader.entity.bookstore;

import com.zhao.myreader.greendao.entity.Book;

import java.util.List;

/**
 * @author zhao
 * @description: 书城小说分类
 * @date :2020/4/13 11:46
 */
public class BookType {


    private String typeName;//分类名称
    private String url;//分类链接

    private List<Book> books; //书列表

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
