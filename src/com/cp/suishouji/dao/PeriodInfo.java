package com.cp.suishouji.dao;

public class PeriodInfo {
	public double income;
	public double cost;
	public void insert(double buyerMoney,int type){
		switch (type) {
		case 1:
			income +=buyerMoney;
			break;

		default:
			cost += buyerMoney;
			break;
		}
	}
}
