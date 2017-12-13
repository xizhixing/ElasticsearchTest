package com.dafy.elasticsearch.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 对象工具类
 * 
 * @author dell
 * */
public class ObjectTools {
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @return
	 * 		true:空字符串
	 * 		false:字符串不为空
	 * */
	public static boolean isNullByString(String str){
		if(str == null || "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断对象是否为空
	 * 
	 * @return
	 * 		true:空对象
	 * 		false:对象不为空
	 * */
	public static boolean isNullByObject(Object obj){
		if(obj == null){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据类名生成对应的类对象
	 * */
	public static Object createObject(String className){
		if(isNullByString(className)){
			throw new RuntimeException("利用反射生成类,类名不能为空.");
		}
		Object obj = null;
		try{
			obj = Class.forName(className).newInstance();
		}catch(Exception e){
			throw new RuntimeException("根据类名" + className + "动态生成类异常.",e);
		}
		return obj;
	}
	
	/**
	 * 将Set转换为List
	 * @param set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List setToList(Set set) {
		List list = new ArrayList();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
}
