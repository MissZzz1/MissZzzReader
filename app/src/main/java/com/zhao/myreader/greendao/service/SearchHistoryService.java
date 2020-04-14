package com.zhao.myreader.greendao.service;

import android.database.Cursor;

import com.zhao.myreader.greendao.GreenDaoManager;

import com.zhao.myreader.greendao.entity.SearchHistory;
import com.zhao.myreader.greendao.gen.SearchHistoryDao;
import com.zhao.myreader.util.DateHelper;
import com.zhao.myreader.util.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


/**
 * Created by zhao on 2017/8/3.
 */

public class SearchHistoryService extends BaseService {

    private ArrayList<SearchHistory> findSearchHistorys(String sql, String[] selectionArgs) {
        ArrayList<SearchHistory> searchHistories = new ArrayList<>();
        try {
            Cursor cursor = selectBySql(sql, selectionArgs);
            if (cursor == null) return searchHistories;
            while (cursor.moveToNext()) {
                SearchHistory searchHistory = new SearchHistory();
                searchHistory.setId(cursor.getString(0));
                searchHistory.setContent(cursor.getString(1));
                searchHistory.setCreateDate(cursor.getString(2));
                searchHistories.add(searchHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return searchHistories;
        }
        return searchHistories;
    }

    /**
     * 返回所有历史记录（按时间从大到小）
     * @return
     */
    public ArrayList<SearchHistory> findAllSearchHistory() {
        String sql = "select * from search_history order by create_date desc";
        return findSearchHistorys(sql, null);
    }


    /**
     * 添加历史记录
     * @param searchHistory
     */
    public void addSearchHistory(SearchHistory searchHistory) {
        searchHistory.setId(UUID.randomUUID().toString());
        searchHistory.setCreateDate(DateHelper.longToTime(new Date().getTime()));
        addEntity(searchHistory);
    }

    /**
     * 删除历史记录
     * @param searchHistory
     */
    public void deleteHistory(SearchHistory searchHistory){
        deleteEntity(searchHistory);
    }

    /**
     * 清空历史记录
     */
    public void clearHistory(){
        SearchHistoryDao searchHistoryDao = GreenDaoManager.getInstance().getSession().getSearchHistoryDao();
        searchHistoryDao.deleteAll();
    }

    /**
     * 根据内容查找历史记录
     * @param content
     * @return
     */
    public SearchHistory findHistoryByContent(String content){
        SearchHistory searchHistory = null;
        String sql = "select * from search_history where content = ?";
        Cursor cursor = selectBySql(sql,new String[]{content});
        if (cursor == null) return searchHistory;
        if (cursor.moveToNext()){
            searchHistory = new SearchHistory();
            searchHistory.setId(cursor.getString(0));
            searchHistory.setContent(cursor.getString(1));
            searchHistory.setCreateDate(cursor.getString(2));
        }
        return searchHistory;
    }

    /**
     * 添加或更新历史记录
     * @param history
     */
    public void addOrUpadteHistory(String history){
        SearchHistory searchHistory = findHistoryByContent(history);
        if (searchHistory == null){
            searchHistory = new SearchHistory();
            searchHistory.setContent(history);
            addSearchHistory(searchHistory);
        }else {
            searchHistory.setCreateDate(DateHelper.longToTime(new Date().getTime()));
            updateEntity(searchHistory);
        }
    }

}
