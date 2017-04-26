package com.jkdroid.johnny.matchbox.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * @author John
 */
public class SharedPreferencesUtils {

	public static final String SP_NAME="userinfo";
	private static SharedPreferences sp;
	
	/**
	 * 通过key获取对应的Boolean值
	 * @param context 上下文
	 * @param key      
	 * @param defValue 默认值
	 * @return boolean值
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue){
		if(sp == null)
			sp =context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 通过key保存Boolean值
	 * @param context 上下文
	 * @param key
	 * @param value 要保存的boolean值
	 */
	public static void saveBoolean(Context context, String key, boolean value){
		if(sp==null){
			sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * 通过key获取对应的String值
	 * @param context 上下文
	 * @param key      
	 * @param defValue 默认值
	 * @return String值
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	/**
	 * 通过key保存String值
	 * @param context 上下文
	 * @param key
	 * @param value 要保存的String值
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}


	/**
	 * 通过key获取对应的String值
	 * @param context 上下文
	 * @param key
	 * @param defValue 默认值
	 * @return String值
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key,defValue);
	}

	/**
	 * 通过key保存String值
	 * @param context 上下文
	 * @param key
	 * @param value 要保存的String值
	 */
	public static void saveInt(Context context, String key, int value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}


	public static boolean containsPreferences(Context context, String key) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.contains(key);
	}



	/**
	 * 通过key删除值
	 * @param context
	 * @param key
	 */
	public static void removePreferences(Context context, String key){
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
}
