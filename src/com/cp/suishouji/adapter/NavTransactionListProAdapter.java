package com.cp.suishouji.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.NavMonthTransactionActivity;
import com.cp.suishouji.NavYearTransactionActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.fragment.BuyerAccountFragment;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.widgt.CpButton2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NavTransactionListProAdapter extends AbstractAdapter {
	private Context context;
	private ArrayList<TransactionInfo> transationList;
	private HashMap<Integer,Integer> typeMap;
	private TranslateAnimation animation;
	private OnMoneyChangeListener moneyChangeListener;
	public NavTransactionListProAdapter(Context context,
			ArrayList<TransactionInfo> transationList,HashMap<Integer,Integer> typeMap) {
		super();
		this.transationList = transationList;
		this.context = context;
		this.typeMap = typeMap;
	}
	/**
	 * 回调接口,当数据发生变化的时候,更新界面
	 * @author cp
	 *
	 */
	public interface OnMoneyChangeListener {
		public abstract void onMoneyChange();
	}
	public void setOnMoneyChangeListener(OnMoneyChangeListener moneyChangeListener){
		this.moneyChangeListener = moneyChangeListener;
	}
	@Override
	public int getCount() {
			return transationList.size();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//CP 耗时
//		MyUtil.logTime("NavTransactionListProAdapter:getView:Start:position:"+position);
//		if(typeMap==null){
//			return LayoutInflater.from(context).inflate(R.layout.item_nav_exlist_empty, null);
//		}
		final View layout = LayoutInflater.from(context).inflate(R.layout.item_navtransaction_listview, null);
		final TransactionInfo info = transationList.get(position);
		TextView tv_category = (TextView) layout.findViewById(R.id.tv_category);
		TextView tv_expense = (TextView) layout.findViewById(R.id.tv_expense);
		TextView tv_imcome = (TextView) layout.findViewById(R.id.tv_imcome);
		TextView tv_date = (TextView) layout.findViewById(R.id.tv_date);
		ImageView img_icon = (ImageView) layout.findViewById(R.id.img_icon);
		String str = null;
		if(info.type==1){//收入
			tv_expense.setVisibility(View.GONE);
			tv_imcome.setVisibility(View.VISIBLE);
			tv_imcome.setText("¥ "+MyUtil.doubleFormate(info.buyerMoney));
			str = "[收入]";
		}else{//支出
			tv_imcome.setVisibility(View.GONE);
			tv_expense.setVisibility(View.VISIBLE);
			tv_expense.setText("¥ "+MyUtil.doubleFormate(info.buyerMoney));
			str = "[支出]";
		}
			tv_category.setText(info.name);
			img_icon.setImageResource(info.imgResId);
		if((typeMap.get(position)&2)==2){//头
			TextView tv_dayofmonth = (TextView) layout.findViewById(R.id.tv_dayofmonth);
			TextView tv_xingqi = (TextView) layout.findViewById(R.id.tv_xingqi);
			tv_dayofmonth.setText(MyUtil.getDate(info.tradeTime));
			tv_xingqi.setText(MyUtil.getDay(info.tradeTime));
			tv_dayofmonth.setVisibility(View.VISIBLE);
			tv_xingqi.setVisibility(View.VISIBLE);
			MyUtil.getDate(System.currentTimeMillis());
//			Log.e("position", position+","+MyUtil.getDay(info.tradeTime)+","+MyUtil.getDate(info.tradeTime));
		}
		if((typeMap.get(position)&1)==1){//尾
			TextView tv_divider1 = (TextView) layout.findViewById(R.id.tv_divider1);
//			TextView tv_divider2 = (TextView) layout.findViewById(R.id.tv_divider2);
			tv_divider1.setVisibility(View.VISIBLE);
//			tv_divider2.setVisibility(View.VISIBLE);
		}
		Date date = new Date(info.tradeTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		tv_date.setText(sdf.format(date)+str);
//		MyUtil.logTime("NavTransactionListProAdapter:getView:End:position:"+position);
		
		
		layout.findViewById(R.id.relativeLayout2).setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				View hideview = layout.findViewById(R.id.hide_editview);
				if(hidenView!=null){
					hidenView.clearAnimation();
					hidenView.setVisibility(View.INVISIBLE);
				}else{
					hidenView =hideview;
					//					shownPosition = position;
					MenuListener munuListener = new MenuListener(info,position);
					hideview.findViewById(R.id.nav_listitem_edit).setOnClickListener(munuListener);
					hideview.findViewById(R.id.nav_listitem_delete).setOnClickListener(munuListener);
					hideview.findViewById(R.id.nav_listitem_copy).setOnClickListener(munuListener);
					hideview.findViewById(R.id.nav_listitem_toincome).setOnClickListener(munuListener);
					hideview.findViewById(R.id.nav_listitem_totrans).setOnClickListener(munuListener);
					if(info.type==0){
						((CpButton2)hideview.findViewById(R.id.nav_listitem_toincome)).setString("改为收入");
					}else{
						((CpButton2)hideview.findViewById(R.id.nav_listitem_toincome)).setString("改为支出");
					}
					initScrollAnimation();
					hideview.startAnimation(animation);
					hideview.setVisibility(View.VISIBLE);
				}
				return true;
			}
			
		});
		return layout;
	}
	
	class MenuListener implements OnClickListener{
		private TransactionInfo info;
		private int position;
		public MenuListener(TransactionInfo info,int position) {
			super();
			this.info = info;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Log.e("菜单点击info", "id:"+v.getId()+" TransactionInfo:"+info.toString());
			switch (v.getId()) {
			case R.id.nav_listitem_edit:
				edit(position,info);
				break;
			case R.id.nav_listitem_delete:
				delete(position,info);
				break;
			case R.id.nav_listitem_copy:
				copy(position,info);
				break;
			case R.id.nav_listitem_toincome:
				toincome(position,info);
				break;
			case R.id.nav_listitem_totrans:
				
				break;

			default:
				break;
			}
		}
		/**
		 * 收入转支出,支出转收入
		 * @param position2
		 * @param info2
		 */
		private void toincome(int position2, TransactionInfo info2) {
			SQLiteDatabase db = DataBaseUtil.getDb();
			ContentValues values = new ContentValues();
//			values.put("type", info.type^1);
			if(info2.type==0){
				info2.type=1;
				DataBaseUtil.modify(2*info2.buyerMoney, info2.buyerAccountPOID);
			}else{
				info2.type=0;
				DataBaseUtil.modify(-2*info2.buyerMoney, info2.buyerAccountPOID);
			}
			values.put("type", info2.type);
			db.update("t_transaction", values, "transactionPOID=?", new String[]{String.valueOf(info2.transactionPOID)});
			notifyDataSetChanged();
			//更新headerview,改变时,由activity去更新headview
			if(moneyChangeListener!=null){
				moneyChangeListener.onMoneyChange();
			}
		}

		private void copy(int position2, TransactionInfo info2) {
			Intent intent = new Intent(context,AddOrEditTransActivity.class);
			Bundle extras = new Bundle();
			extras.putParcelable("transactioninfo", info2);
			intent.putExtras(extras);
			intent.putExtra("mode", 2);//复制模式:1
			if(context instanceof NavMonthTransactionActivity){
			((NavMonthTransactionActivity)context).startActivityForResult(intent, 0);
			}else if(context instanceof NavYearTransactionActivity){
			((NavYearTransactionActivity)context).startActivityForResult(intent, 0);
			}
		} 

		private void delete(int position2, final TransactionInfo info2) {
			new AlertDialog.Builder(context).setTitle("提示") 
			.setMessage("确认要删除记录?")
			.setNegativeButton("取消", null)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SQLiteDatabase db = DataBaseUtil.getDb();
					db.delete("t_transaction", "transactionPOID=?", new String[]{String.valueOf(info2.transactionPOID)});
					if(info2.type!=1){
						DataBaseUtil.modify(info2.buyerMoney, info2.buyerAccountPOID);
					}else{
						DataBaseUtil.modify(-info2.buyerMoney, info2.buyerAccountPOID);
					}
					Toast.makeText(context, "删除成功!", 0).show();
					transationList.remove(position);
					notifyDataSetChanged();
					//更新headerview,改变时,由activity去更新headview
					if(moneyChangeListener!=null){
						moneyChangeListener.onMoneyChange();
					}
				}
			}).create().show();
		}

		private void edit(int position2, TransactionInfo info2) {
			Intent intent = new Intent(context,AddOrEditTransActivity.class);
			Bundle extras = new Bundle();
			extras.putParcelable("transactioninfo", info2);
			intent.putExtras(extras);
			intent.putExtra("mode", 1);//编辑模式:1
			if(context instanceof NavMonthTransactionActivity){
			((NavMonthTransactionActivity)context).startActivityForResult(intent, 0);
			}else if(context instanceof NavYearTransactionActivity){
			((NavYearTransactionActivity)context).startActivityForResult(intent, 0);
			}
		}
		
	}
//	private int shownPosition;
	private View hidenView;
//	private View lastLongClickView;
	public boolean isShown(){
		if(hidenView==null){
			return false;
		}
		return true;
	}
	public void hideMunuView(){
		hidenView.clearAnimation();
		hidenView.setVisibility(View.INVISIBLE);
		hidenView = null;
	}
//	public int getPosition(){
//		return shownPosition;
//	}
	public View getShownView(){
		return hidenView;
	}
	private void initScrollAnimation(){
		if(animation==null){
			animation = new TranslateAnimation(MyUtil.dip2px(context, 180), 0 , 0, 0);
			animation.setDuration(500);
			animation.setFillAfter(true);
		}
	}
}
