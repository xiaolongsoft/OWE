package com.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.demo.dao.BillDao;
import com.demo.entity.QFentity;

@SpringBootApplication
public class CalculateApplication {
		public static void main(String[] args) {
			 ConfigurableApplicationContext run = SpringApplication.run(CalculateApplication.class, args);
			 BillDao dao=(BillDao) run.getBean("billDao");
			 List<QFentity> list=dao.listQF();
			 Map<String,ArrayList<QFentity>> map=new TreeMap<>();
			 ArrayList<QFentity> tmp = null;
			 for(QFentity qf:list){
				 if(!map.containsKey(qf.getHaoma())){
					 tmp=new ArrayList<QFentity>();
					 map.put(qf.getHaoma(), tmp);
				 }
				 tmp.add(qf);
			 }
			 Set<String> set=map.keySet();
			 	for(Iterator<String> it=set.iterator();it.hasNext();){
			 		String haoma=it.next();
			 		List<QFentity> l=sort(map.get(haoma));
			 		for(QFentity q:l){
			 			System.out.println("haoma:【"+q.getHaoma()+"】date:"+q.getDate()+"money:"+q.getMoney());
			 			dao.excuteSql("INSERT INTO `result_171219` (haoma,money,DATE) VALUES ('"+q.getHaoma()+"','"+q.getMoney()+"','"+q.getDate()+"')");
			 		}
			 	}	
		}
		
		
		public static List<QFentity> sort(List<QFentity> list){
				Collections.sort(list); //按日期从远到近排序
				QFentity tmp=null;  //对比项
				for(Iterator<QFentity> it=list.iterator();it.hasNext();){
					QFentity q=it.next();
					if(compareTo(tmp, q)){ //是否符号条件可以去除
						it.remove();
					}
				tmp=q;
				}
				for(QFentity q:list){
					q.setDate(localdateConverntRange(dateToLocalDate(q.getDate()))); //将日期转换为区间
				}
			return list;
			
		}
		
		/**
		 * 对比两项的 账期和金额  判断是否可以替换
		 * @param o1 对比项
		 * @param o2  当前项
		 * @return
		 */
		public static boolean compareTo(QFentity o1,QFentity o2){
			if(o1==null||o1.getMoney().compareTo(o2.getMoney())!=0){
				return false;
			};
			String str=o1.getDate();
			String str2=o2.getDate();
			//对比项位日期
			LocalDate d1=dateToLocalDate(str);
			//当前项日期
			LocalDate d2=dateToLocalDate(str2);
			
			//1.当前项是否在对比项的后6个月内
			if(d2.minusMonths(6).isBefore(d1)){
				return true;
			};
			return false;
		}
		
		//字符串转日期
		public static LocalDate dateToLocalDate(String date){
			return LocalDate.of(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(4,6)), 1);
		}
		
		//日期转区间
		public  static String localdateConverntRange(LocalDate localdate){
			LocalDate min=localdate.minusMonths(3);
			LocalDate max=localdate.plusMonths(6);
			return min.format(DateTimeFormatter.BASIC_ISO_DATE).substring(0, 6)+"-"+max.format(DateTimeFormatter.BASIC_ISO_DATE).substring(0, 6);
		}
		
		@Test
		public void test(){
			
			System.out.println(localdateConverntRange(dateToLocalDate("201601")));
		}

}
