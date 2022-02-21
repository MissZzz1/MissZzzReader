package com.zhao.myreader.util.crawler;

import android.net.UrlQuerySanitizer;
import android.text.Html;

import com.zhao.myreader.common.URLCONST;
import com.zhao.myreader.entity.bookstore.BookType;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
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
 * 天籁小说网html解析工具
 * Created by zhao on 2017/7/24.
 */

public class TianLaiReadUtil {


    /**
     * 从html中获取章节正文
     *
     * @param html
     * @return
     */
    public static String getContentFormHtml(String html) {

        Document doc = Jsoup.parse(html);
        Element divContent = doc.getElementById("content");

        if (divContent != null) {
            String content = Html.fromHtml(divContent.html()).toString();
            char c = 160;
            String spaec = "" + c;
            content = content.replace(spaec, "  ");
            return content;
        } else {
            return "";
        }
    }

    /**
     * 从html中获取章节列表
     *
     * @param html
     * @return
     */
    public static ArrayList<Chapter> getChaptersFromHtml(String html,Book book) {
        ArrayList<Chapter> chapters = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element divList = doc.getElementById("list");
        Element dl = divList.getElementsByTag("dl").get(0);

        String lastTile = null;
        int i = 0;

        for(Element dd : dl.getElementsByTag("dd")){
            Elements as = dd.getElementsByTag("a");
            if (as.size() > 0) {
                Element a = as.get(0);
                String title = a.html();
                if (!StringHelper.isEmpty(lastTile) && title.equals(lastTile)) {
                    continue;
                }
                Chapter chapter = new Chapter();
                chapter.setNumber(i++);
                chapter.setTitle(title);
                String url = a.attr("href");
                if (StringHelper.isEmpty(book.getSource())
                        || BookSource.tianlai.toString().equals(book.getSource())) {
                    url = URLCONST.nameSpace_tianlai + url;
                } else if (BookSource.biquge.toString().equals(book.getSource())) {
                    url = book.getChapterUrl() + url;
                }else if (BookSource.dingdian.toString().equals(book.getSource())) {
                    url = URLCONST.nameSpace_dingdian + url;
                }
                chapter.setUrl(url);
                chapters.add(chapter);
                lastTile = title;
            }

        }

        return chapters;
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

        Element div = doc.getElementsByClass("details").first();


        for (Element element : div.getElementsByClass("item-pic")) {
            Book book = new Book();
            Element img = element.getElementsByTag("img").first();
            book.setImgUrl(URLCONST.nameSpace_tianlai + img.attr("src"));
            Element info = element.getElementsByClass("result-game-item-detail").first();

            for (Element el : info.children()) {

                String infoStr = el.text();

                if (el.tagName().equals("h3")){

                    Element a = el.getElementsByTag("a").first();
                    book.setChapterUrl(URLCONST.nameSpace_tianlai + a.attr("href"));
                    book.setName(a.text());

                }else if(el.className().equals("intro")){
                    book.setDesc(el.text());

                }else if (infoStr.contains("作者：")) {
                    infoStr = infoStr.substring(0,infoStr.indexOf("状态"));
                    book.setAuthor(infoStr.replace("作者：", "").replace(" ", ""));
                } else if (infoStr.contains("类型：")) {
                    book.setType(infoStr.replace("类型：", "").replace(" ", ""));
                } else if (infoStr.contains("更新时间：")) {
                    book.setUpdateDate(infoStr.replace("更新时间：", "").replace(" ", ""));
                }else if(infoStr.contains("最新章节")) {
                    Element newChapter = el.getElementsByTag("a").first();
                    book.setNewestChapterUrl(URLCONST.nameSpace_tianlai + newChapter.attr("href"));
                    book.setNewestChapterTitle(newChapter.text());
                }

            }

            book.setSource(BookSource.tianlai.toString());
            books.add(book);

        }

        return books;
    }


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
            bookType.setTypeName(h3.text().replace("小说排行榜",""));


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
                book.setChapterUrl(URLCONST.nameSpace_tianlai + a.attr("href"));
                book.setSource(BookSource.tianlai.toString());

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
        book.setSource(BookSource.tianlai.toString());
        Document doc = Jsoup.parse(html);


        Element meta = doc.getElementsByAttributeValue("property","og:url").get(0);

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

        //作者
        Element p0 = ps.get(0);

        Pattern pattern = Pattern.compile("作&nbsp;&nbsp;&nbsp;&nbsp;者：(.*)");
        Matcher matcher = pattern.matcher(p0.html());
        if (matcher.find()){
            book.setAuthor(matcher.group(1));
        }


        //类别
        Element a = doc.getElementsByClass("con_top").first().getElementsByTag("a").get(1);

        book.setType(a.text());





        //更新时间
        Element p3 = ps.get(2);

        pattern = Pattern.compile("最后更新：(.*)");
        matcher = pattern.matcher(p3.html());
        if (matcher.find()){
            book.setUpdateDate(matcher.group(1));
        }


        //最新章节
        Element p2 = ps.get(3);
        a = p2.getElementsByTag("a").get(0);
        book.setNewestChapterTitle(a.text());
        book.setNewestChapterUrl(a.attr("href"));



        //简介
        Element pIntro = doc.getElementById("intro").getElementsByTag("p").first();
        book.setDesc(Html.fromHtml(pIntro.html()).toString());




        return book;

    }


    /**
     * 获取热门小说列表
     * @param html
     * @return
     */
    public static List<String> getHotBookList(String html)  {

        Element doc = Jsoup.parse(html);
        List<String> hotBooks  = new ArrayList<>();

        Elements items = new Elements();


        //全部热门推荐
        Elements allHots = doc.getElementsByClass("item");

        items.addAll(allHots);

        //各分类热门推荐

        Elements typeHot = doc.getElementsByClass("content");


        items.addAll(typeHot);

        //提取书名
        for(Element item : items){
            Element a = item.getElementsByTag("dt").first().getElementsByTag("a").first();
            hotBooks.add(a.text());
        }


        return hotBooks;

    }

}
