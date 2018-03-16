package com.demo.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.demo.entity.Analyze;
import com.demo.entity.Bill;
import com.demo.entity.QFentity;
@Mapper
public interface BillDao {
				//插入全量数据
			 Integer  insertBills(List<Bill> bills);
			 List<Analyze>  selectAnalyze(@Param(value="table")String table,@Param(value="table0")String table0);
			 //清空表
			 void excuteSql(@Param(value="sql")String sql);
			 @Select("SELECT COUNT(batchId) FROM `t_qianfei_cuijiao_his` WHERE batchId=#{batchId}")
			 Integer findBatchIdFromHis(@Param(value="batchId")String batchId);
			 
			 @Select("SELECT * FROM `calculate_171219` WHERE haoma IN (SELECT haoma FROM `calculate_171219` GROUP BY haoma   HAVING   COUNT(haoma)>1)")
			 List<QFentity> listQF();
}
