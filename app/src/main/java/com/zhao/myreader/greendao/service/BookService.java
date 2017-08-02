package com.zhao.myreader.greendao.service;

import android.database.Cursor;

import com.zhao.myreader.greendao.GreenDaoManager;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.gen.BookDao;
import com.zhao.myreader.util.StringHelper;

import java.util.List;

/**
 * Created by zhao on 2017/7/24.
 */

public class BookService extends BaseService {

    private ChapterService mChapterService;

    public BookService(){
        mChapterService = new ChapterService();
    }

    /**
     * 通过ID查书
     * @param id
     * @return
     */
    public Book getBookById(String id) {
        BookDao bookDao = GreenDaoManager.getInstance().getSession().getBookDao();
        return bookDao.load(id);
    }

    /**
     * 获取所有的书
     * @return
     */
    public List<Book> getAllBooks(){
        BookDao bookDao = GreenDaoManager.getInstance().getSession().getBookDao();
        return bookDao.loadAll();
    }

    /**
     * 新增书
     * @param book
     */
    public void addBook(Book book){
        book.setId(StringHelper.getStringRandom(25));
        addEntity(book);
    }

    /**
     * 查找书（作者、书名）
     * @param author
     * @param bookName
     * @return
     */
    public Book findBookByAuthorAndName(String bookName, String author){
        Book book = null;
        try {
            Cursor cursor = selectBySql("select id from book where author = ? and name = ?",new String[]{author,bookName});
            if (cursor.moveToNext()){
                String id = cursor.getString(0);
                book = getBookById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    /**
     * 删除书
     * @param id
     */
    public void deleteBookById(String id){
        BookDao bookDao = GreenDaoManager.getInstance().getSession().getBookDao();
        bookDao.deleteByKey(id);
        mChapterService.deleteBookALLChapterById(id);
    }

    /**
     * 删除书
     * @param book
     */
    public void deleteBook(Book book){
       deleteEntity(book);
        mChapterService.deleteBookALLChapterById(book.getId());
    }
}
