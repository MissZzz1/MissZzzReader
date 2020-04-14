package com.zhao.myreader.util.crawler;

import android.text.Html;

import com.zhao.myreader.entity.bookstore.BookType;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.util.DateHelper;
import com.zhao.myreader.util.StringHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 天籁小说网html解析工具
 * Created by zhao on 2017/7/24.
 */

public class BiQuGeReadUtil {


    /**
     * 获取书城小说分类列表
     * @param html
     * @return
     */
    public static List<BookType> getBookTypeList(String html){
        List<BookType> bookTypes = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        Elements divs = doc.getElementsByClass("nav");
        if (divs.size() > 0){
            Elements uls = divs.get(0).getElementsByTag("ul");
            if (uls.size() > 0){
                for(Element li : uls.get(0).children()){
                    Element a = li.child(0);
                    BookType bookType = new BookType();
                    bookType.setTypeName(a.attr("title"));
                    bookType.setUrl(a.attr("href"));
                    if (!bookType.getTypeName().contains("小说") || bookType.getTypeName().contains("排行")) continue;
                    if (StringHelper.isNotEmpty(bookType.getTypeName())){
                        bookTypes.add(bookType);
                    }

                }
            }

        }
        return bookTypes;

    }

    /**
     * 获取某一分类小说排行榜列表
     * @param html
     * @return
     */
    public static List<Book> getBookRankList(String html){
        List<Book> books = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements divs = doc.getElementsByClass("r");
        if (divs.size() > 0){
            Elements uls = divs.get(0).getElementsByTag("ul");
            if (uls.size() > 0){
                for(Element li : uls.get(0).children()){
                    Book book = new Book();
                    Element scanS1 = li.getElementsByClass("s1").get(0);
                    Element scanS2 = li.getElementsByClass("s2").get(0);
                    Element scanS5 = li.getElementsByClass("s5").get(0);
                    book.setType(scanS1.html().replace("[","").replace("]",""));
                    Element a = scanS2.getElementsByTag("a").get(0);
                    book.setName(a.attr("title"));
                    book.setChapterUrl(a.attr("href"));
                    book.setAuthor(scanS5.html());
                    book.setSource(BookSource.biquge.toString());
                    books.add(book);

                }
            }
        }

        return books;

    }

    /**
     * 获取小说详细信息
     * @param html
     * @return
     */
    public static Book getBookInfo(String html,Book book)  {


        //小说源
        book.setSource(BookSource.biquge.toString());
        Document doc = Jsoup.parse(html);

        //图片url
        Element divImg = doc.getElementById("fmimg");
        Element img = divImg.getElementsByTag("img").get(0);
        book.setImgUrl(img.attr("src"));
        Element divInfo = doc.getElementById("info");

        //书名
        Element h1 = divInfo.getElementsByTag("h1").get(0);
        book.setName(h1.html());

        Elements ps = divInfo.getElementsByTag("p");

        //作者
        Element p0 = ps.get(0);
        Element a = p0.getElementsByTag("a").get(0);
        book.setAuthor(a.html());

        //更新时间
        Element p2 = ps.get(2);

        Pattern pattern = Pattern.compile("更新时间：(.*)&nbsp;");
        Matcher matcher = pattern.matcher(p2.html());
        if (matcher.find()){
            book.setUpdateDate(matcher.group(1));
        }

        //最新章节
        Element p3 = ps.get(3);
        a = p3.getElementsByTag("a").get(0);
        book.setNewestChapterTitle(a.attr("title"));
        book.setNewestChapterUrl(book.getChapterUrl() + a.attr("href"));

        //简介
        Element divIntro = doc.getElementById("intro");
        book.setDesc(Html.fromHtml(divIntro.html()).toString());


        return book;

    }






}
