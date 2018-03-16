package com.demo.entity;

import java.math.BigDecimal;

public class QFentity implements Comparable<QFentity>{
	private String haoma;
	private String date;
	private BigDecimal money;
	public String getHaoma() {
		return haoma;
	}
	public void setHaoma(String haoma) {
		this.haoma = haoma;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	@Override
	public int compareTo(QFentity o) {
			
		return Integer.valueOf(this.getDate())-Integer.valueOf(o.getDate());
	}
	
	
}
