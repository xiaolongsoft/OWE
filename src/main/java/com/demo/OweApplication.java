package com.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.demo.dao.BillDao;
import com.demo.entity.Analyze;
import com.demo.utils.MyDateUtils;
import com.demo.utils.MyExcelUtils;

@SpringBootApplication
public class OweApplication {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		System.out.println("请输入分析文件的名称：(文件默认放置在当前项目qfFile文件夹下,名称格式  : 201705****.xlsx)");
		String filePath=sc.next();
		String mon="";
		String flag="N";
		if(filePath.split("\\.")[0].length()>5){
			mon=filePath.substring(0, 6);
		}
		if(!"".equals(mon)){
			System.out.println("请输入当月数据文档日期   (对应格式: 201705 ).");
			System.err.println("当前识别的账期为："+mon );
			System.err.println("确认使用该账期  ? (Y/N)  【确定/手动输入】");
			flag=sc.next();
		}
		
		if("N".equalsIgnoreCase(flag)){
			System.out.println("请输入当月数据文档日期   (对应格式:   201705 )");
			mon=sc.next();
		}
		sc.close();
        ConfigurableApplicationContext run = SpringApplication.run(OweApplication.class, args);
		BillDao dao=(BillDao) run.getBean("billDao");
		System.out.println("start load excel data...");
		long start=System.currentTimeMillis();
			dao.excuteSql("   truncate table owe_all_tmp; "); // 1 .1  清空总表
			dao.excuteSql("   truncate table owe_gh_tmp; ");  //1.2清空固话表
			dao.excuteSql("   truncate table owe_jth_tmp; "); //1.3清空集团号表
    		dao.insertBills(ReadExcel.readExeclData(filePath));                // 2  插入总数据
    		
    		System.out.println("LoadData Finished:"+(System.currentTimeMillis()-start));
    		start=System.currentTimeMillis();
    		dao.excuteSql(" INSERT INTO `owe_gh_tmp` SELECT * FROM `owe_all_tmp` WHERE LENGTH(groupid)=11; "); //3.1插入固话表       
    		dao.excuteSql(" INSERT INTO `owe_jth_tmp` SELECT * FROM `owe_all_tmp` WHERE LENGTH(groupid)=10; "); //3.2插入集团号表  
    		dao.excuteSql("  DROP VIEW IF exists gid_ls; "); //清空临时表
    		dao.excuteSql(" CREATE VIEW  gid_ls  AS SELECT groupid,stattime FROM  `owe_gh_tmp` GROUP BY groupid HAVING COUNT(DISTINCT stattime)=1 AND stattime='"+mon+"' ;"); //只欠当前一月账期的用户
    		dao.excuteSql(" DELETE FROM  owe_gh_tmp WHERE groupid IN ( SELECT groupid FROM gid_ls ) "); //删除
    		dao.excuteSql("  DROP VIEW IF exists gid_ls; "); //清空临时表
    		dao.excuteSql(" CREATE VIEW  gid_ls  AS select DISTINCT groupid from owe_gh_tmp where stattime  NOT in("+MyDateUtils.getFourMonth(mon)+");");
    		dao.excuteSql(" DELETE FROM  owe_gh_tmp WHERE groupid  IN ( SELECT groupid FROM gid_ls ) ");//删除四个月内无欠款的用户
      		dao.excuteSql("  DROP table IF EXISTS owe_gh"+mon+"_last;	  ");                //上个月的总数据
    		dao.excuteSql(" CREATE table owe_gh"+mon+"_last AS "
                            +"SELECT groupid lid, SUM(bill_three_qf) lsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ltime"
                            + "  FROM owe_gh_tmp  WHERE  stattime<>'"+mon+"'  GROUP BY groupid ");   
    		dao.excuteSql("  DROP table IF EXISTS owe_gh"+mon+"_now;	  ");				 //本月的总数据		 	
    		dao.excuteSql(" CREATE table owe_gh"+mon+"_now AS "
    				+"SELECT groupid nid, SUM(bill_three_qf) nsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ntime"
    				+ "  FROM owe_gh_tmp    GROUP BY groupid ");   
    		dao.excuteSql(" DROP table IF EXISTS owe_jth"+mon+"_last;	 ");
    		dao.excuteSql(" CREATE table owe_jth"+mon+"_last AS "
                    +"SELECT groupid lid, SUM(bill_three_qf) lsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ltime"
                    + "  FROM owe_jth_tmp  WHERE  stattime<>'"+mon+"'  GROUP BY groupid ");   
    		dao.excuteSql("  DROP table IF EXISTS owe_jth"+mon+"_now;	  ");				 //本月的总数据		 	
    		dao.excuteSql(" CREATE table owe_jth"+mon+"_now AS "
			+"SELECT groupid nid, SUM(bill_three_qf) nsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ntime"
			+ "  FROM owe_jth_tmp    GROUP BY groupid ");
    		System.out.println("over!");
    		List<Analyze> gh=dao.selectAnalyze("gh"+mon,"gh"+MyDateUtils.getLastMon(mon));
    		MyExcelUtils.writeToExcel(changeListToArr(gh), "固话欠费-上月本月对比("+mon+"经分).xlsx");
    		List<Analyze> jth=dao.selectAnalyze("jth"+mon,"jth"+MyDateUtils.getLastMon(mon));
    		MyExcelUtils.writeToExcel(changeListToArr(jth),"集团号欠费-上月本月对比("+mon+"经分).xlsx");
    		
    		System.out.println("save in his....");
    		dao.excuteSql("INSERT INTO	`t_qianfei_cuijiao_his` SELECT * FROM `t_qianfei_cuijiao` ");
    		dao.excuteSql("  truncate table t_qianfei_cuijiao; ");
			dao.excuteSql(" DROP VIEW IF EXISTS `tmp_qianfei_view` ");
			StringBuilder sb=new StringBuilder("CREATE VIEW `tmp_qianfei_view` AS  SELECT  `owe_all_tmp`.`user_state` AS `a1`,");
			sb.append("SUBSTR(`owe_all_tmp`.`groupid`, '4') AS `a2`,");
			sb.append("SUM(`owe_all_tmp`.`bill_three_qf`) AS `a3`,");
			sb.append("`owe_all_tmp`.`account_name` AS `a4` FROM `owe_all_tmp` WHERE (");
			sb.append("  `owe_all_tmp`.`bill_two` = '挂机短信费' ) GROUP BY `owe_all_tmp`.`user_state` ");
			dao.excuteSql(sb.toString());
			dao.excuteSql("INSERT INTO `t_qianfei_cuijiao`  (user_state,number,total_price,REAL_NAME )    SELECT a1,a2,a3,a4 FROM `tmp_qianfei_view`");
			dao.excuteSql("UPDATE  t_qianfei_cuijiao SET batchId='"+mon+"'");
			System.out.println("All Finished:"+(System.currentTimeMillis()-start));
    		
	}
	

	
		public static List<String[]> changeListToArr(List<Analyze> list){
			 List<String[]>  arr=new LinkedList<String[]>();
			 String[] names={"受理号码","上月欠费金额","上月欠费账期","本月欠费金额","本月欠费账期","本月与上月重合账期欠费金额","本月与上月重合账期","差值(上月欠费金额-本月与上月重合账期欠费金额)"};
			 String[] analyzes=null;
		
			 arr.add(names);
			 for(Analyze a:list){
				 analyzes=new String[8];
				 analyzes[0]=a.getId();
				 analyzes[1]=a.getLsum()+"";
				 analyzes[2]=a.getLtime()+"";
				 analyzes[3]=a.getNsum()+"";
				 analyzes[4]=a.getNtime()+"";
				 analyzes[5]=a.getSum()+"";
				 analyzes[6]=a.getTime()+"";
				 analyzes[7]=a.getS()+"";
				 arr.add(analyzes);
			 }
			return arr;
		}
}
