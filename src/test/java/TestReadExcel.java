import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.util.StringUtils;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.jconsole.DruidPanel;
import com.demo.ReadExcel;
import com.demo.dao.BillDao;
import com.demo.entity.Bill;
import com.demo.utils.MyExcelUtils;  
  
/** 
 * 测试类 
 *  
 * @author ChuanJing 
 * 
 */  
public class TestReadExcel {  
  
    public static void main(String[] args) throws Exception {  
    	Scanner sc=new Scanner(System.in);
    	String s=	sc.nextLine();
		System.out.println(s);
		String s2=sc.next();
		System.out.println(s2);
		sc.close();
	
    } 
    
    
    public static SqlSessionFactory  getSqlSseesionFactory() throws FileNotFoundException {
    	Map<String, String> properties=new HashMap<String,String>();
    	properties.put("username", "root");
    	properties.put("password", "root");
    	properties.put("url", "jdbc:mysql://192.168.1.177:3306/test?useUnicode=true&amp;characterEncoding=UTF-8");
    	properties.put("driverClassName", "com.alibaba.druid.pool.DruidDataSource");
 
    	DataSource dataSource=null;
		try {
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			System.out.println("数据库链接异常！");
			e.printStackTrace();
		}
    	 String resource = "E:\\myWorkSpace_5_6\\Owe\\src\\main\\java\\mybatis-config.xml";
    	 File file=new File(resource);
    	 FileInputStream fis=new FileInputStream(file);
    	SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(fis);
	    
	    	return sqlSessionFactory;
    }
    
    @Test
    public void  xxxxxxxxx() throws IOException {
 /*   	 XSSFWorkbook wb = new XSSFWorkbook();
    	 XSSFSheet  sheet= wb.createSheet("mysheet");
    	 sheet.setDefaultColumnWidth(15);
    	 XSSFRow row= sheet.createRow(0);
    	 XSSFCell cell0=row.createCell(0);
    	 cell0.setCellValue("啦啦啦啦啦啦啦");
     	 XSSFCell cell1=row.createCell(1);
    	 cell1.setCellValue("pooooooooo");
    	 XSSFCell cell2=row.createCell(2);
    	 cell2.setCellValue("5555555555555555");
    	 XSSFCell cell3=row.createCell(3);
    	 cell3.setCellValue("qqqqqqqqqqqqqqqqfdfd对方");
    	 FileOutputStream outputStream=new FileOutputStream("D:/aaa.xlsx");
    	 wb.write(outputStream);*/
    	
    }
    
  
}  