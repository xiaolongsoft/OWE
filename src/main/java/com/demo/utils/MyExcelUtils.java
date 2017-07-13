package com.demo.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MyExcelUtils {
		private MyExcelUtils(){
			
		}

	/**
	 * 将结果写入Excel中
	 * @param list   写入Excel中的数据数组 
	 * @param fileName   生成excel的文件名称
	 */
	public static  void writeToExcel(List<String[]> arr,String fileName){
		 XSSFWorkbook wb = new XSSFWorkbook();
		 XSSFSheet   sheet=	wb.createSheet();
		 sheet.setDefaultColumnWidth(15);
		 XSSFRow  row=null;
		 
		 for(int i=0;i<arr.size();i++){
			 row= sheet.createRow(i);
			 String[] ar=arr.get(i);
			 for(int j=0;j<ar.length;j++){
				 XSSFCell cell= row.createCell(j);
				 cell.setCellValue(ar[j]);
			 }
		 }

		 	FileOutputStream outputStream;
			try {
				outputStream = new FileOutputStream(System.getProperty("user.dir")+"/src/qfFile/"+fileName);
				wb.write(outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}

}
