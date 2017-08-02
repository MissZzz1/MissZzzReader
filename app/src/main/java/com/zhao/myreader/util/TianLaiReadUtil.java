package com.zhao.myreader.util;

import android.text.Html;

import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

        Pattern pattern = Pattern.compile("<div id=\"content\">[\\s\\S]*?</div>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String content = Html.fromHtml(matcher.group(0)).toString();
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
    public static ArrayList<Chapter> getChaptersFromHtml(String html) {
        ArrayList<Chapter> chapters = new ArrayList<>();
        Pattern pattern = Pattern.compile("<dd><a href=\"([\\s\\S]*?)</a>");
        Matcher matcher = pattern.matcher(html);
        int i = 0;
        while (matcher.find()) {
            Chapter chapter = new Chapter();
            String content = matcher.group();
            chapter.setNumber(i++);
            chapter.setTitle(content.substring(content.indexOf("\">") + 2, content.lastIndexOf("<")));
            chapter.setUrl(content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\"")));
            chapters.add(chapter);
        }
        return chapters;
    }

    /**
     * 从搜索html中得到书列表
     * @param html
     * @return
     */
    public static ArrayList<Book> getBooksFromSearchHtml(String html) {
        ArrayList<Book> books = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element node = doc.getElementById("results");
        for (Element div : node.children()) {
            if (!StringHelper.isEmpty(div.className()) && div.className().equals("result-list")) {
                for (Element element : div.children()) {
                    Book book = new Book();
                    Element img = element.child(0).child(0).child(0);
                    book.setImgUrl( img.attr("src"));
                    Element title  = element.getElementsByClass("result-item-title result-game-item-title").get(0);
                    book.setName(title.child(0).attr("title"));
                    book.setChapterUrl(title.child(0).attr("href"));
                    Element desc = element.getElementsByClass("result-game-item-desc").get(0);
                    book.setDesc(desc.text());
                    Element info = element.getElementsByClass("result-game-item-info").get(0);
                    for (Element element1 : info.children()){
                        String infoStr = element1.text();
                        if (infoStr.contains("作者：")){
                            book.setAuthor(infoStr.replace("作者：","").replace(" ",""));
                        }else if (infoStr.contains("类型：")){
                            book.setType(infoStr.replace("类型：","").replace(" ",""));
                        }else if (infoStr.contains("更新时间：")){
                            book.setUpdateDate(infoStr.replace("更新时间：","").replace(" ",""));
                        }else {
                            Element newChapter = element1.child(1);
                            book.setNewestChapterUrl(newChapter.attr("href"));
                            book.setNewestChapterTitle(newChapter.text());
                        }
                    }
                    books.add(book);
                }
            }
        }

        return books;
    }

}
