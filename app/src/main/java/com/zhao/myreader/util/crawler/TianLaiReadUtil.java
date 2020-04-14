package com.zhao.myreader.util.crawler;

import android.text.Html;

import com.zhao.myreader.common.URLCONST;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.util.StringHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
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
                if (StringHelper.isEmpty(book.getSource()) || BookSource.tianlai.toString().equals(book.getSource())) {
                    url = URLCONST.nameSpace_tianlai + url;
                } else if (BookSource.biquge.toString().equals(book.getSource())) {
                    url = book.getChapterUrl() + url;
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
//        Element node = doc.getElementById("results");
//        for (Element div : node.children()) {
        Elements divs = doc.getElementsByClass("result-list");
        Element div = divs.get(0);
//        if (!StringHelper.isEmpty(div.className()) && div.className().equals("result-list")) {
        for (Element element : div.children()) {
            Book book = new Book();
            Element img = element.child(0).child(0).child(0);
            book.setImgUrl(img.attr("src"));
            Element title = element.getElementsByClass("result-item-title result-game-item-title").get(0);
            book.setName(title.child(0).attr("title"));
            book.setChapterUrl(title.child(0).attr("href"));
            Element desc = element.getElementsByClass("result-game-item-desc").get(0);
            book.setDesc(desc.text());
            Element info = element.getElementsByClass("result-game-item-info").get(0);
            for (Element element1 : info.children()) {
                String infoStr = element1.text();
                if (infoStr.contains("作者：")) {
                    book.setAuthor(infoStr.replace("作者：", "").replace(" ", ""));
                } else if (infoStr.contains("类型：")) {
                    book.setType(infoStr.replace("类型：", "").replace(" ", ""));
                } else if (infoStr.contains("更新时间：")) {
                    book.setUpdateDate(infoStr.replace("更新时间：", "").replace(" ", ""));
                } else {
                    Element newChapter = element1.child(1);
                    book.setNewestChapterUrl(newChapter.attr("href"));
                    book.setNewestChapterTitle(newChapter.text());
                }
            }
            book.setSource(BookSource.tianlai.toString());
            books.add(book);

        }

        return books;
    }

}
