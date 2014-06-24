package com.cp.suishouji.utils;

import android.widget.TextView;

public class MyCalculator {
	/**
	 * 计算器,结果输出到 TextView tv_calcuresult
	 */
	private TextView tv_calcuresult;
	
	public MyCalculator(TextView tv_calcuresult ) {
		super();
		this.tv_calcuresult = tv_calcuresult;
		this.result = 0;
	}
	public MyCalculator(TextView tv_calcuresult,double result) {
		super();
		this.tv_calcuresult = tv_calcuresult;
		this.result = result;
	}
	int sign;//记录加减
	double result;//暂存结果
	String strnum = "0";//input未输入初始值
	String tempstrnum = "0";//input未输入初始值
	String xiaoshu = "";//小数未输入初始值
	int ADD = 1;//加法
	int DEC = 2;//减法
	private boolean countover;
	
	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	// 用于格式化输出到textview
	private String formatStr(String str) {
		return MyUtil.formatStr(str, xiaoshu);
	}

	/**
	 *  按下的是数字
	 * @param str
	 */
	public void pressNum(String str) {
		if(countover){
			result = 0;
			countover = false;
		}
		if(strnum.length()>8){
			return;
		}
		// 没有按加减,清空result
		if (sign == 0) {
			if (result != 0) {
				result = 0;
			}
		}
		// 第一个数字
		if ("0".equals(strnum)) {
			if ("".equals(xiaoshu)) {
				strnum = str;
				tv_calcuresult.setText(strnum);
				return;
			}
		}
		if ("".equals(xiaoshu)) {
			// 整数位
			strnum += str;
		} else {
			// 开始按小数
			if (xiaoshu.length() > 2) {
				String substring = xiaoshu.substring(1);
				int temp = Integer.valueOf(substring);
				temp++;
				temp %= 100;
				if (temp < 10) {
					xiaoshu = ".0" + String.valueOf(temp);
				} else {
					xiaoshu = "." + String.valueOf(temp);
				}
			} else {
				xiaoshu += str;
			}
		}
		tv_calcuresult.setText(formatStr(strnum));
	}
	
	public void clear(){
		pressClear();
	}
	/**
	 *  清除
	 */
	public void pressClear() {
		tv_calcuresult.setText("0");
		strnum = "0";
		xiaoshu = "";
		result = 0;
		sign = 0;
	}

	/**
	 *  小数点
	 */
	public void pressDot() {
		if ("".equals(xiaoshu)) {
			xiaoshu = ".";
			tv_calcuresult.setText(formatStr(strnum));

		}
	}

	/**
	 * 按=
	 */
	public void count() {
		//有小数
		if (!"".equals(xiaoshu) && !".".equals(xiaoshu)) {
			strnum += xiaoshu;
		}
		if(!"0".equals(strnum)){
			tempstrnum =strnum;
		}
		if (sign == ADD) {// 加法
			result += Double.valueOf(tempstrnum);
		} else if (sign == DEC) {// 减法
				result -= Double.valueOf(tempstrnum);
		}else {
			result = Double.valueOf(strnum);
		}
		tv_calcuresult.setText(MyUtil.doubleFormate(result));
//		formatDouble(result);
//		sign = 0;
		strnum = "0";
		xiaoshu = "";
		countover = true;
	}
	/**
	 * 按+,-
	 * @param value
	 */
	public void count(int value) {
		// 是否有输入值
		if (!"0".equals(strnum) || xiaoshu.length() > 1) {
			if (!"".equals(xiaoshu) && !".".equals(xiaoshu)) {
				strnum += xiaoshu;
			}
			if (sign == DEC) {
				result -= Double.valueOf(strnum);// 保存中间值
			} else {
				result += Double.valueOf(strnum);// 保存中间值
			}
			tempstrnum = strnum;
//			formatDouble(result);
			tv_calcuresult.setText(MyUtil.doubleFormate(result));
			strnum = "0";
			xiaoshu = "";
		}
		sign = value;
	}
}
