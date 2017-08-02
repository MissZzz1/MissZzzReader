package com.zhao.myreader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SharedPreferencesUtils {
	
	private static Editor mEditor;
	
	private SharedPreferences mSharedPreferences;
	/**
	 * 
	 * @param context
	 */
	public SharedPreferencesUtils(Context context){
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		mEditor = mSharedPreferences.edit();
	}
	/**
	 * 
	 * @param context
	 * @param fileName
	 */
	public SharedPreferencesUtils(Context context, String fileName){
		mSharedPreferences = context
				.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	/**
	 * 设置通知公告是否已被查看
	 * @param key
	 * @param flag
     */
	public void setNoticeFlag(String key, boolean flag) {
		mEditor.putBoolean(key,flag);
		mEditor.commit();
	}

	/**
	 * 判断在该机该用户的通知公告是否已被查看
	 * @param key
	 * @return
     */
	public boolean isNoticeFlag(String key){
		return mSharedPreferences.getBoolean(key,false);
	}

	
	/**
	 * 保存数据
	 * @param key  键值
	 * @param news 需要保存的数据
	 */
	public void save(String key, String news) {
		Set<String> values = null;
		//获取或创建key,如果xml文件中含有key
		if(mSharedPreferences.contains(key)){
			//将数据存储到集合里
			values = mSharedPreferences.getStringSet(key, null);
			//删除xml文件中key,并提交
			mEditor.remove(key).commit();
		}else{//如果xml文件中没有key
			values = new HashSet<String>();
		}
		//将新的数据添加到集合中
		values.add(news);
		//将集合保存到偏好设置中
		mEditor.putStringSet(key, values);
		//提交
		mEditor.commit();
	}
	
	/**
	 * 清空数据
	 */
	public void clear(){
		mEditor.clear();
		mEditor.commit();
	}
	/**
	 * 获取数据
	 * @param key 键值
	 * @return 返回一个字符串
	 */
	public List<String> query(String key){
		//创建StringBuffer对象，用来存储从偏好设置中获取的数据
		List<String> list = new ArrayList<String>();
		//获取xml中对应key的值
		Set<String> values = mSharedPreferences.getStringSet(key, null);
		//判断该数据是否为空，如果不为空，则
		if(key != null && values != null){
			//遍历集合
			Iterator<String> iterator = values.iterator();
			while(iterator.hasNext()){
				//将数据添加到StringBuffer
				list.add(iterator.next());
			}
		}
		return list;
	}

	/**
	 * 设置是否是第一次登录
	 * @param values
     */
	public void setFirstEntry(int values){
		mEditor.putInt("first_entry",values);
		mEditor.commit();
	}

	public int getFirstEntry(){
		return mSharedPreferences.getInt("first_entry",-1);
	}
	

}
