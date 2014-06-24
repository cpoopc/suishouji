package com.cp.suishouji.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyUtil {
	static String TAG = "Myutil";
	//ms-->日期
	static Calendar mcalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
	static String[] dayofweek = new String[]{"0.0","周日","周一","周二","周三","周四","周五","周六"};
	
	static long currenttime = 0;
	static long lastttime = 0;
	
	/**
	 */
	public static boolean isHighDpi(Context context){
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//		Log.e("metrics.heightPixels", "metrics.heightPixels"+metrics.heightPixels+"dp:"+px2dip(context, metrics.heightPixels));
//		Toast.makeText(context, "dp:"+px2dip(context, metrics.heightPixels), 0).show();
		if(metrics.heightPixels>=dip2px(context, 570)){
			return true;
		}
//		if(metrics.densityDpi>=DisplayMetrics.DENSITY_HIGH){
//			return true;
//		}
		return false;
	}
	public static int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

		switch (widthMode) {
		/**
		 * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
		 * MeasureSpec.AT_MOST。
		 * 
		 * 
		 * MeasureSpec.EXACTLY是精确尺寸，
		 * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
		 * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
		 * 
		 * 
		 * MeasureSpec.AT_MOST是最大尺寸，
		 * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
		 * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
		 * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
		 * 
		 * 
		 * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
		 * 通过measure方法传入的模式。
		 */
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = widthSize;
			break;
		}
		return result;
	}

	public static  int measureHeight(int pHeightMeasureSpec) {
		int result = 0;

		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

		switch (heightMode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = heightSize;
			break;
		}
		return result;
	}
	static public float getAngle(float startAngle,float stopAngle){
		float angle = stopAngle - startAngle;
		if(angle<0){
			angle +=360;
		}
		return 0;
	}
	
	static public float[] getPercent(double ...d){
		if(d.length == 0){
			return null;
		}
		double total = 0;
		for (int i = 0; i < d.length; i++) {
			total +=d[i];
		}
		float[] percents = new float[d.length];
		float totalpercents = 0;
		for (int i = 0; i < percents.length-1; i++) {
			percents[i] = (float) (d[i]/total);
			totalpercents +=percents[i];
		}
		percents[percents.length-1] = 1-totalpercents;
		return percents;
	}
	/**
	 * 设置line长度
	 */
	static public void setLength(Context context,LayoutParams paramsIn,LayoutParams paramsEx,LayoutParams paramsTotal,
			double income,double expense,double total){
		float dip2px = dip2px(context, 100);
			double max = Math.max(income, expense);
			paramsIn.width = (int) (dip2px * income /max) ;
			paramsEx.width = (int) (dip2px * expense /max) ;
			paramsTotal.width = (int) Math.abs((dip2px * total /max)) ;
//		}
		
	}
	/**
	 * 获得当月最大天数
	 * @param year
	 * @param month 0base
	 * @return
	 */
	static public int getMaxDayOfMonth(int year,int month){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	
//	public static int dip2px(Context context, float dpValue) {
//	    final float scale = context.getResources().getDisplayMetrics().density;
//	    return (int) (dpValue * scale + 0.5f);
//	}
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 性能分析:耗时
	 */
	static public void logTime(){
		logTime("默认");
	}
	static public void logTime(String str){
		currenttime = System.currentTimeMillis();
		Log.e("性能分析:耗时 ::"+str, "last: "+lastttime+", now: "+currenttime+", 耗时:"+ (currenttime-lastttime)+"毫秒");
		lastttime = currenttime;
	}
	static public void logTime(int tag){
		currenttime = System.currentTimeMillis();
		Log.e("性能分析:耗时["+tag+"]", "last: "+lastttime+", now: "+currenttime+", 耗时:"+ (currenttime-lastttime)+"毫秒");
		lastttime = currenttime;
	}
	/**
	 * 	返回给定年月的毫秒时间
	 * @param year
	 * @param month 0-base
	 * @return
	 */
	static public long getYearMonthMillis(int year,int month){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		calendar.set(year, month, 1, 0, 0, 0);
		return calendar.getTimeInMillis();
	}
	/**
	 * 获得给定时间(milliseconds)下一天0点的milliseconds
	 * @param tradetime milliseconds
	 * @return milliseconds
	 */
	//testcode
//	Log.e(TAG, "now:"+java.lang.System.currentTimeMillis()+"tomorrow"+MyUtil.getNextDay(java.lang.System.currentTimeMillis())+"");
//	Log.e(TAG, "now:"+MyUtil.getDate(java.lang.System.currentTimeMillis())+"tomorrow:"+MyUtil.getDate(MyUtil.getNextDay(java.lang.System.currentTimeMillis())));
	static public long getNextDay(long milliseconds){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		calendar.setTimeInMillis(milliseconds);
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), today+1, 0, 0, 0);
		return calendar.getTimeInMillis();
	}
	/**
	 * 获得给定时间(milliseconds)当天0点的milliseconds
	 * @param tradetime milliseconds
	 * @return milliseconds
	 */
	static public long getCurrentDay(long milliseconds){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		calendar.setTimeInMillis(milliseconds);
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), today, 0, 0, 0);
		return calendar.getTimeInMillis();
	}
	/**
	 * 
	 * @param milliseconds
	 * @return	"周日","周一","周二","周三","周四","周五","周六"
	 */
	static public String getDay(long milliseconds){
		mcalendar.setTimeInMillis(milliseconds);
		int day = mcalendar.get(Calendar.DAY_OF_WEEK);
		return dayofweek[day];
	}
	/**
	 * 
	 * @param milliseconds
	 * @return	day of month
	 */
	static public String getDate(long milliseconds){
//		Log.e(TAG, "getDate");
		mcalendar.setTimeInMillis(milliseconds);
		int dayofmonth = mcalendar.get(Calendar.DAY_OF_MONTH);
		String day;
		if(dayofmonth<10){
			day = "0"+String.valueOf(dayofmonth);
		}else{
			day = String.valueOf(dayofmonth);
		}
		return day;
	}
//	/**
//	 * 
//	 * @return todayMillis	本日第一刻的毫秒时间
//	 */
//	static public long getTodayMillis(){
//		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
//		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
////		Log.e("calendar2.getTimeInMillis()", calendar.getTimeInMillis()+"");
//		long todayMillis = calendar.getTimeInMillis();
//		return todayMillis;
//	} 
	/**
	 * 
	 * @return todayMillis	当日第一刻的毫秒时间
	 */
	static public long getDayMillis(int shift){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//		Log.e("calendar2.getTimeInMillis()", calendar.getTimeInMillis()+"");
		long todayMillis = calendar.getTimeInMillis() + 1000*3600*24l*shift;
		return todayMillis;
	} 
	/**
	 * 
	 * @return weekMillis	本周第一刻的毫秒时间
	 */
	static public long getWeekMillis(int shift){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		return  getDayMillis(7*shift) - (calendar.get(Calendar.DAY_OF_WEEK)-1)*24*3600*1000;
	} 
	/**
	 * 
	 * @return monthMillis	本月第一刻的毫秒时间
	 */
	static public long getMonthMillis(int shift){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		int shift_year = 0;
		int shift_month = 0;
		int cur_month = calendar.get(Calendar.MONTH);
		if(shift>=0){
			shift_year = (shift + cur_month) / 12;
			if(shift + cur_month> 11 ){
				shift_month = (shift - cur_month) % 12;
			}else{
				shift_month = shift;
			}
		}else{//shift<0
			shift_year = (int) Math.floor(((float)(shift + cur_month)) / 12);
			if(shift + cur_month< 0 ){
				shift_month = 12 + (shift + cur_month)%12 - cur_month;
			}else{
				shift_month = shift;
			}
		}
		calendar.set(calendar.get(Calendar.YEAR) + shift_year, cur_month + shift_month, 1, 0, 0, 0);
		long monthMillis = calendar.getTimeInMillis();
		return  monthMillis;
	} 
	/**
	 * 
	 * @param 带","的数字String
	 * @return double
	 */
	static public double String2double(String result){
		String[] split = result.split(",");
		String temp = "";
		for (int i = 0; i < split.length; i++) {
			temp +=split[i];
		}
		return Double.valueOf(temp);
	}
	/**
	 * double转xxx,xxx,xxx.xx
	 * @param double
	 * @return String 科学型
	 */
	static public String doubleFormate(double d){
//		BigDecimal b = new BigDecimal(d);
//		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		String valueOf = String.valueOf(f1);
		DecimalFormat df = new DecimalFormat("#0.00");
		String valueOf = df.format(d);
		int indexOf = valueOf.indexOf(".");
		if(valueOf.startsWith("-")){
			String str1 = valueOf.substring(1, indexOf);
			String str2 = valueOf.substring(indexOf);
			return "-"+formatStr(str1, str2);
		}else{
			String str1 = valueOf.substring(0, indexOf);
			String str2 = valueOf.substring(indexOf);
			return formatStr(str1, str2);
		}
		
	}
	static public double double2(double d){
		BigDecimal b = new BigDecimal(d);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
	/**
	 * 
	 * @param str	整数位
	 * @param xiaoshu	小数位(带".")
	 * @return
	 */
	public static String formatStr(String str, String xiaoshu) {
		StringBuilder stringBuilder = new StringBuilder();
		int length = str.length();
		int nums = (length-1) / 3;
		int position =nums;
		for (int i = 0; i < length; i++) {
			stringBuilder.append(str.charAt(i));
				if (i == length - 3 * position  - 1 && position>0) {
					position --;
					stringBuilder.append(",");
				}
		}
		String string = stringBuilder.toString();
		if (xiaoshu!=null&&!"".equals(xiaoshu)) {
			string += xiaoshu;
		}
		return string;
	}
}
