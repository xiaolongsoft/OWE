package com.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.demo.dao.BillDao;
import com.demo.entity.Analyze;
import com.demo.entity.Bill;
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
    		dao.insertBills(readExeclData(filePath));                // 2  插入总数据
    		
    		System.out.println("LoadData Finished:"+(System.currentTimeMillis()-start));
    		start=System.currentTimeMillis();
    		dao.excuteSql(" INSERT INTO `owe_gh_tmp` SELECT * FROM `owe_all_tmp` WHERE LENGTH(groupid)=11; "); //3.1插入固话表       
    		dao.excuteSql(" INSERT INTO `owe_jth_tmp` SELECT * FROM `owe_all_tmp` WHERE LENGTH(groupid)=10; "); //3.2插入集团号表  
    		dao.excuteSql("  DROP VIEW IF exists gid_ls; "); //清空临时表
    		dao.excuteSql(" CREATE VIEW  gid_ls  AS SELECT groupid,stattime FROM  `owe_gh_tmp` GROUP BY groupid HAVING COUNT(DISTINCT stattime)=1 AND stattime='"+mon+"' ;"); //只欠当前一月账期的用户
    		dao.excuteSql(" DELETE FROM  owe_gh_tmp WHERE groupid IN ( SELECT groupid FROM gid_ls ) "); //删除
    		dao.excuteSql("  DROP VIEW IF exists gid_ls; "); //清空临时表
    		dao.excuteSql(" CREATE VIEW  gid_ls  AS select DISTINCT groupid from qianfei_all_201702_guhua where stattime in("+MyDateUtils.getFourMonth(mon)+");");
    		dao.excuteSql(" DELETE FROM  owe_gh_tmp WHERE groupid IN ( SELECT groupid FROM gid_ls ) ");//删除四个月内无欠款的用户
      		dao.excuteSql("  DROP VIEW IF EXISTS owe_gh_view_last;	  ");                //上个月的总数据
    		dao.excuteSql(" CREATE VIEW owe_gh_view_last AS "
                            +"SELECT groupid lid, SUM(bill_three_qf) lsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ltime"
                            + "  FROM owe_gh_tmp  WHERE  stattime<>'"+mon+"'  GROUP BY groupid ");   
    		dao.excuteSql("  DROP VIEW IF EXISTS owe_gh_view_now;	  ");				 //本月的总数据		 	
    		dao.excuteSql(" CREATE VIEW owe_gh_view_now AS "
    				+"SELECT groupid nid, SUM(bill_three_qf) nsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ntime"
    				+ "  FROM owe_gh_tmp    GROUP BY groupid ");   
    		dao.excuteSql(" DROP VIEW IF EXISTS owe_jth_view_last;	 ");
    		dao.excuteSql(" CREATE VIEW owe_jth_view_last AS "
                    +"SELECT groupid lid, SUM(bill_three_qf) lsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ltime"
                    + "  FROM owe_jth_tmp  WHERE  stattime<>'"+mon+"'  GROUP BY groupid ");   
    		dao.excuteSql("  DROP VIEW IF EXISTS owe_jth_view_now;	  ");				 //本月的总数据		 	
    		dao.excuteSql(" CREATE VIEW owe_jth_view_now AS "
			+"SELECT groupid nid, SUM(bill_three_qf) nsum ,GROUP_CONCAT(DISTINCT stattime ORDER BY stattime) ntime"
			+ "  FROM owe_jth_tmp    GROUP BY groupid ");      		
    		
    		List<Analyze> gh=dao.selectAnalyze("gh");
    		MyExcelUtils.writeToExcel(changeListToArr(gh), "固话欠费-上月本月对比("+mon+"经分).xlsx");
    		List<Analyze> jth=dao.selectAnalyze("jth");
    		MyExcelUtils.writeToExcel(changeListToArr(jth),"集团号欠费-上月本月对比("+mon+"经分).xlsx");
    		System.out.println("All Finished:"+(System.currentTimeMillis()-start));
    		
	}
	
	/**
	 * 读取excel文件内容
	 * @return
	 */
	public static List<Bill>   readExeclData(String filePath){
        ReadExcel re = new ReadExcel();  
        List<List<String>> list = re.read(System.getProperty("user.dir")+"/src/qfFile/"+filePath,1);//忽略前5行  
        List<Bill>  bills=new ArrayList<Bill>();
        // 遍历读取结果  
        if (list != null) {  
        		Bill b=null;
            for (int i = 1; i < list.size(); i++) {  
                List<String> cellList = list.get(i);  
                //过滤非挂机短信类
               if(!("挂机短信费".equals(cellList.get(9))&&(cellList.get(0).startsWith("0106")||cellList.get(0).startsWith("0108")||cellList.get(0).startsWith("1")))) continue;
                b=new Bill();
                	//用户号码
                	b.setGroupid(cellList.get(0));
                	//用户标识、
                	b.setUser_state(cellList.get(1));
                	//客户名称
                	b.setUser_name(cellList.get(2));
                	//融合固话
                	b.set融合固话(cellList.get(3));
                	//客户标识
                	b.set客户标识(cellList.get(4));
                	//账户名称
                	b.setAccount_name(cellList.get(5));
                	//账户标识	
                	b.setAccount_state(cellList.get(6));
                	//账期
                	b.setStattime(cellList.get(7));
                	//一级账单名称
                	b.setBill_one(cellList.get(8));
                	//二级账单名称
                	b.setBill_two(cellList.get(9));
                	//三级账单名称
                	b.setBill_three(cellList.get(10));
                	//三级账单ID
                	b.setBill_three_id(cellList.get(11));
                	//三级账单欠费金额
                	if(cellList.get(12)!=null&&cellList.get(12)!=""){
                		b.setBill_three_qf(Double.valueOf(cellList.get(12)));
                	}
                	bills.add(b);
            }  
        }
        return bills;
	}
	
		public static List<String[]> changeListToArr(List<Analyze> list){
			 List<String[]>  arr=new LinkedList<String[]>();
			 String[] names={"受理号码","上月欠费金额","上月欠费账期","本月欠费金额","本月欠费账期","本月与上月重合账期欠费金额","本月与上月重合账期"};
			 String[] analyzes=null;
		
			 arr.add(names);
			 for(Analyze a:list){
				 analyzes=new String[7];
				 analyzes[0]=a.getId();
				 analyzes[1]=a.getLsum()+"";
				 analyzes[2]=a.getLtime()+"";
				 analyzes[3]=a.getNsum()+"";
				 analyzes[4]=a.getNtime()+"";
				 analyzes[5]=a.getSum()+"";
				 analyzes[6]=a.getTime()+"";
				 arr.add(analyzes);
			 }
			return arr;
		}
}
