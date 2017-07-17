package com.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateUtils {

		/**
		 * 根据传进来的月份   返回最近的四个月日期拼接串
		 * @param month
		 * @return
		 */
			public static String getFourMonth(String month){
		        SimpleDateFormat formatter;
		        
		        formatter = new SimpleDateFormat ("yyyyMM"); 
		        Date aa=new Date();
				try {
					aa = formatter.parse(month);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        Calendar cal_1=Calendar.getInstance();//获取当前日期 
		        StringBuilder sb=new StringBuilder(month);
		        cal_1.setTime(aa);
		        for(int i=1;i<4;i++){
		        	cal_1.add(Calendar.MONTH, -1);
		        	sb.append(",'");
		        	sb.append(cal_1.get(Calendar.YEAR));
		        	int m=cal_1.get(Calendar.MONTH)+1;
		        	if(m<10){
		        		sb.append("0");
		        	}
		        	sb.append(m);
		        	sb.append("'");
		        }
		        return sb.toString();
				
			}
			/**
			 * 返回上一个月日期
			 * @param mon
			 * @return
			 */
			public  static String getLastMon(String month){
		        SimpleDateFormat formatter;
		        
		        formatter = new SimpleDateFormat ("yyyyMM"); 
		        Date aa=new Date();
				try {
					aa = formatter.parse(month);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        Calendar cal_1=Calendar.getInstance();//获取当前日期 
		        cal_1.setTime(aa);
		        cal_1.add(Calendar.MONTH, -1);
		        int m=cal_1.get(Calendar.MONTH)+1;
		        StringBuilder sb=new StringBuilder("");
	        	sb.append(cal_1.get(Calendar.YEAR));
	        	if(m<10){
	        		sb.append("0");
	        	}
	        	sb.append(m);
	 
		        return 	sb.toString();	        	
			}
	

}
