package com.demo.entity;
import java.io.Serializable;

public class Bill  implements  Serializable{
	  

	private static final long serialVersionUID = 1L;

	private String groupid;//
	  
	private String user_state;//
	  
	private String user_name;//
	  
	private String 融合固话;//
	  
	private String 客户标识;//
	  
	private String account_name;//
	  
	private String account_state;//
	  
	private String stattime;//
	  
	private String bill_one;//
	  
	private String bill_two;//
	  
	private String bill_three;//
	  
	private String bill_three_id;//
	
	private Double bill_three_qf;
	  
	public String getGroupid() {
		 return groupid;
	}
	
	public void setGroupid(String groupid) {
		 this.groupid = groupid;
	}
	
	public String getUser_state() {
		 return user_state;
	}
	
	public void setUser_state(String user_state) {
		 this.user_state = user_state;
	}
	
	public String getUser_name() {
		 return user_name;
	}
	
	public void setUser_name(String user_name) {
		 this.user_name = user_name;
	}
	
	public String get融合固话() {
		 return 融合固话;
	}
	
	public void set融合固话(String 融合固话) {
		 this.融合固话 = 融合固话;
	}
	
	public String get客户标识() {
		 return 客户标识;
	}
	
	public void set客户标识(String 客户标识) {
		 this.客户标识 = 客户标识;
	}
	
	public String getAccount_name() {
		 return account_name;
	}
	
	public void setAccount_name(String account_name) {
		 this.account_name = account_name;
	}
	
	public String getAccount_state() {
		 return account_state;
	}
	
	public void setAccount_state(String account_state) {
		 this.account_state = account_state;
	}
	
	public String getStattime() {
		 return stattime;
	}
	
	public void setStattime(String stattime) {
		 this.stattime = stattime;
	}
	
	public String getBill_one() {
		 return bill_one;
	}
	
	public void setBill_one(String bill_one) {
		 this.bill_one = bill_one;
	}
	
	public String getBill_two() {
		 return bill_two;
	}
	
	public void setBill_two(String bill_two) {
		 this.bill_two = bill_two;
	}
	
	public String getBill_three() {
		 return bill_three;
	}
	
	public void setBill_three(String bill_three) {
		 this.bill_three = bill_three;
	}
	
	public String getBill_three_id() {
		 return bill_three_id;
	}
	
	public void setBill_three_id(String bill_three_id) {
		 this.bill_three_id = bill_three_id;
	}

	public Double getBill_three_qf() {
		return bill_three_qf;
	}

	public void setBill_three_qf(Double bill_three_qf) {
		this.bill_three_qf = bill_three_qf;
	}
	
}
