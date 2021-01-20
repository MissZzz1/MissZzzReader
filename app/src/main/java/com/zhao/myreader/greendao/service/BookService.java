package com.zhao.myreader.greendao.service;

import android.database.Cursor;
import android.widget.ListView;

import com.zhao.myreader.greendao.GreenDaoManager;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.gen.BookDao;
import com.zhao.myreader.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhao on 2017/7/24.
 */

public class BookService extends BaseService {

    private ChapterService mChapterService;

    public BookService(){
        mChapterService = new ChapterService();
    }

    private List<Book> findBooks(String sql, String[] selectionArgs) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            Cursor cursor = selectBySql(sql, selectionArgs);
            while (cursor.moveToNext()) {
                Book book = new Book();
                book.setId(cursor.getString(0));
                book.setName(cursor.getString(1));
                book.setChapterUrl(cursor.getString(2));
                book.setImgUrl(cursor.getString(3));
                book.setDesc(cursor.getString(4));
                book.setAuthor(cursor.getString(5));
                book.setType(cursor.getString(6));
                book.setUpdateDate(cursor.getString(7));
                book.setNewestChapterId(cursor.getString(8));
                book.setNewestChapterTitle(cursor.getString(9));
                book.setNewestChapterUrl(cursor.getString(10));
                book.setHistoryChapterId(cursor.getString(11));
                book.setHisttoryChapterNum(cursor.getInt(12));
                book.setSortCode(cursor.getInt(13));
                book.setNoReadNum(cursor.getInt(14));
                book.setChapterTotalNum(cursor.getInt(15));
                book.setLastReadPosition(cursor.getInt(16));
                book.setSource(cursor.getString(17));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
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
        String sql = "select * from book  order by sort_code";
        return findBooks(sql, null);
    }

    /**
     * 新增书
     * @param book
     */
    public void addBook(Book book){
        book.setSortCode(countBookTotalNum() + 1);
        book.setId(UUID.randomUUID().toString());
        addEntity(book);
    }

    /**
     * 查找书（作者、书名）
     * @param author
     * @param bookName
     * @return
     */
    public Book findBookByAuthorAndName(String bookName, String author,String source){
        Book book = null;
        try {
            Cursor cursor = selectBySql("select id from book where author = ? and name = ? and source = ?",new String[]{author,bookName,source});
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

    /**
     * 查询书籍总数
     * @return
     */
    public int countBookTotalNum(){
        int num = 0;
        try {
            Cursor cursor = selectBySql("select count(*) n from book ",null);
            if (cursor.moveToNext()){
                num = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 更新书
     * @param books
     */
    public void updateBooks(List<Book> books){
        BookDao bookDao = GreenDaoManager.getInstance().getSession().getBookDao();
        bookDao.updateInTx(books);
    }

}
