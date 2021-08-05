package com.zhao.myreader.util.crawler;

import android.text.Html;

import com.zhao.myreader.entity.bookstore.BookType;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.util.StringHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 顶点小说html解析工具
 * Created by zhao on 2017/7/24.
 */

public class DingDianReadUtil {


    /**
     * 提取排行榜列表
     * @param html
     * @return
     */
    public static List<BookType> getRank(String html){

        List<BookType> bookTypes = new ArrayList<>();

        Document doc = Jsoup.parse(html);

        Elements boxs = doc.getElementById("main").getElementsByTag("div");

        for(Element box : boxs){
            String className = box.className();
            if (!className.contains("box")) continue;

            BookType bookType = new BookType();


            Element h3 = box.getElementsByTag("h3").get(0);
            bookType.setTypeName(h3.text().replace("小说推荐排行榜",""));


            //书列表
            List<Book> books = new ArrayList<>();
            Elements lis = box.getElementsByTag("li");

            for(Element li : lis){

                if (StringHelper.isNotEmpty(li.className())){
                    continue;
                }

                Book book = new Book();
                Element a = li.getElementsByTag("a").get(0);

                book.setName(a.text());
                book.setChapterUrl(a.attr("href"));
                book.setSource(BookSource.dingdian.toString());

                books.add(book);


            }

            bookType.setBooks(books);

            bookTypes.add(bookType);




        }


        return bookTypes;


    }




    /**
     * 获取小说详细信息
     * @param html
     * @return
     */
    public static Book getBookInfo(String html,Book book)  {


        //小说源
        book.setSource(BookSource.dingdian.toString());
        Document doc = Jsoup.parse(html);


        Element meta = doc.getElementsByAttributeValue("property","og:novel:read_url").get(0);

        book.setChapterUrl(meta.attr("content"));


        //图片url
        Element divImg = doc.getElementById("fmimg");
        Element img = divImg.getElementsByTag("img").get(0);
        book.setImgUrl(img.attr("src"));

        //书名
        Element divInfo = doc.getElementById("info");
        Element h1 = divInfo.getElementsByTag("h1").get(0);
        book.setName(h1.html());

        Elements ps = divInfo.getElementsByTag("p");
        Element a;
        //作者
        Element p0 = ps.get(0);

        Pattern pattern = Pattern.compile("作&nbsp;&nbsp;&nbsp;&nbsp;者：(.*)");
        Matcher matcher = pattern.matcher(p0.html());
        if (matcher.find()){
            book.setAuthor(matcher.group(1));
        }


        //类别
        Element p1 = ps.get(1);
        pattern = Pattern.compile("类&nbsp;别： (.*)");
        matcher = pattern.matcher(p1.html());
        if (matcher.find()){
            book.setType(matcher.group(1));
        }


        //最新章节
        Element p2 = ps.get(2);
        a = p2.getElementsByTag("a").get(0);
        book.setNewestChapterTitle(a.text());
        book.setNewestChapterUrl(a.attr("href"));

        //更新时间
        Element p3 = ps.get(3);
        Element time = p3.getElementsByTag("time").get(0);


        book.setUpdateDate(time.text());
        //简介
        Element divIntro = doc.getElementById("intro");
        book.setDesc(Html.fromHtml(divIntro.html()).toString());




        return book;

    }



    /**
     * 从搜索html中得到书列表
     *
     * @param html
     * @return
     */
    public static ArrayList<Book> getBooksFromSearchHtml(String html) {
        ArrayList<Book> books = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        Element div = doc.getElementById("content");


        if (div != null){

            Elements trs = div.getElementsByTag("tr");
            for (Element tr : trs) {

                if (!"nr".equals(tr.attr("id"))) continue;

                Book book = new Book();

                Elements tds = tr.getElementsByTag("td");

                for(int i = 0; i < tds.size(); i++){
                    Element td = tds.get(i);
                    if (i == 0){
                        Element a = td.getElementsByTag("a").get(0);
                        book.setChapterUrl(a.attr("href"));
                        book.setName(a.text());
                    }else if(i == 1){
                        Element a = td.getElementsByTag("a").get(0);
                        book.setNewestChapterUrl(a.attr("href"));
                        book.setNewestChapterTitle(a.text());
                    }else if(i == 2){

                        book.setAuthor(td.text());
                    }else if(i == 4){
                        book.setUpdateDate(td.text());
                    }
                }



               /* Element s2a  = tr.getElementsByClass("s2").get(0).getElementsByTag("a").get(0);

                book.setChapterUrl(s2a.attr("href"));
                book.setName(s2a.text());

                Element s4 = tr.getElementsByClass("s4").get(0);
                book.setAuthor(s4.text());
                Element s1 = tr.getElementsByClass("s1").get(0);
                book.setType(s1.text());
                Element s5 = tr.getElementsByClass("s5").get(0);
                book.setUpdateDate(s5.text());

                Element s3a = tr.getElementsByClass("s3").get(0).getElementsByTag("a").get(0);

                book.setNewestChapterUrl(s3a.attr("href"));
                book.setNewestChapterTitle(s3a.text());*/

                book.setSource(BookSource.dingdian.toString());
                books.add(book);

            }

        }else{
            Book book = new Book();
            getBookInfo(html,book);
            books.add(book);

        }





        return books;
    }






}
