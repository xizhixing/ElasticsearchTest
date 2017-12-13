package com.dafy.elasticsearch.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * property管理类
 * 
 * @author dell
 * */
public class PropertyManager {
	
	private static PropertyManager proManager = new PropertyManager();
	
	private Map <String,Map<String,String>>proMap = new HashMap<String,Map<String,String>>();
	
	private PropertyManager(){
	}

	public static PropertyManager instance(){
		return proManager;
	}
	
	/**
	 * 获取属性文件中的属性值
	 *  
	 * @return
	 * 		属性值
	 * */
	public String getValue(String propertyFileName,String propertyName){
		return getValue(propertyFileName,propertyName,true);
	}
	
	/**
	 * 获取属性文件中的属性值
	 * 
	 * @param propertyFileName 属性文件名称
	 * @param propertyName 属性名
	 * @param isCache 是否缓存(true:缓存到内存,false:不进行缓存)
	 * 
	 * @return
	 * 		属性值
	 * */
	public String getValue(String propertyFileName,String propertyName,boolean isCache){
		if(ObjectTools.isNullByString(propertyFileName)){
			return null;
		}
		if(ObjectTools.isNullByString(propertyName)){
			return null;
		}
		
		if(!isCache){
			return this.getPropertyValue(propertyFileName, propertyName);
		}
		
		if(!this.proMap.containsKey(propertyFileName)){
			Map <String,String>map = new HashMap<String,String>();
			this.proMap.put(propertyFileName, map);
		}
		
		Map <String,String>map = this.proMap.get(propertyFileName);
		if(!map.containsKey(propertyName)){
			String value = this.getPropertyValue(propertyFileName, propertyName);
			if(value != null && !"".equals(value)){
				map.put(propertyName,value);
			}
		}
		return map.get(propertyName);
	}
	
	/**
	 * 通过属性文件获取指定的属性值
	 * */
	private String getPropertyValue(String propertyFileName,String propertyName){
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream(propertyFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(propertyName);
	}
}
