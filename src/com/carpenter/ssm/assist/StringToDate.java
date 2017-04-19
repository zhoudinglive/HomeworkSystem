package com.carpenter.ssm.assist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**   
* @Title: StringToDate.java 
* @Package com.carpenter.ssm.assist 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月11日 上午9:54:32 
* @version V1.0   
*/

public class StringToDate {
	public static Date stringToDate(String str_time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		Date date = null;
		try {
			date = sdf.parse(str_time);
			if (date == null || date.toString().equals("")) {
				return null;
			}
			return date;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean compareToNow(Date date1){
		Date nowDate=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(nowDate);
		try {
			Date date2 = format.parse(time);
			if(date1.compareTo(date2)<=0){
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int compareIfDate1BiggerThanDate2(Date date1, Date date2){
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time1 = format.format(date1);
		String time2 = format.format(date2);
		
		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
			if(date1.compareTo(date2)<=0){
				return 0;
			}
			return 1;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
}
