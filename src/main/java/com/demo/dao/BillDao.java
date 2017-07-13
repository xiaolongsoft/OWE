package com.demo.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.demo.entity.Analyze;
import com.demo.entity.Bill;
@Mapper
public interface BillDao {
				//插入全量数据
			 Integer  insertBills(List<Bill> bills);
			 List<Analyze>  selectAnalyze(@Param(value="table")String table);
			 //清空表
			 void excuteSql(@Param(value="sql")String sql);
			 
			 
}
