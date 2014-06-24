package com.cp.suishouji.dao;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;

import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 交易信息
 * @author cp
 *	t_transaction 表信息
 */
public class TransactionInfo implements Parcelable{
	public int type;										//交易类型,支出,收入	
	public int transactionPOID;					//交易ID
	public int sellerCategoryPOID;			//category类型id  			表t_category(图片,标题)
	public int buyerAccountPOID;						//账户 								表t_account	
	public long tradeTime;							//交易时间						计算日期
	public double buyerMoney;				//2位小数
	public String memo;								//备注
	
	public int imgResId;							//转化后的图片资源id
	public String name;								//category名
	
	public TransactionInfo() {
		super();
	}

	public TransactionInfo(int type, int transactionPOID,
			int sellerCategoryPOID, int buyerAccountPOID, long tradeTime,
			double buyerMoney, String memo) {
		super();
		this.type = type;
		this.transactionPOID = transactionPOID;
		this.sellerCategoryPOID = sellerCategoryPOID;
		this.buyerAccountPOID = buyerAccountPOID;
		this.tradeTime = tradeTime;
		this.buyerMoney = buyerMoney;
		this.memo = memo;
	}
	
	
	public TransactionInfo(int type, int transactionPOID,
			int sellerCategoryPOID, int buyerAccountPOID, long tradeTime,
			double buyerMoney, String memo, int imgResId, String name) {
		super();
		this.type = type;
		this.transactionPOID = transactionPOID;
		this.sellerCategoryPOID = sellerCategoryPOID;
		this.buyerAccountPOID = buyerAccountPOID;
		this.tradeTime = tradeTime;
		this.buyerMoney = buyerMoney;
		this.memo = memo;
		this.imgResId = imgResId;
		this.name = name;
	}

	//查询某个时间段交易记录
	public static ArrayList<TransactionInfo> query(Context context,long starttime,long stoptime){
		CategoryInfo categoryInfo;
		HashMap<Integer, CategoryInfo> categoryMap = CategoryInfo.getCategoryMap(context);
		if(categoryMap==null){
			Log.e("transactionInfo:static:query", "categoryMap==null");
			return null;
		}
		ArrayList<TransactionInfo> infoList = new ArrayList<TransactionInfo>();
		SQLiteDatabase db = DataBaseUtil.getDb();
		Cursor query = db.query("t_transaction", null, "tradetime>? AND tradetime<?", 
				new String[]{String.valueOf(starttime),String.valueOf(stoptime)}, null, null, "tradetime");
//		boolean toFirst = query.moveToFirst();
		boolean toFirst = query.moveToLast();
		if(!toFirst){
			Log.e("当月没有数据", "xx");
			return null;
		}
		while(toFirst){
			int type = query.getInt(query.getColumnIndex("type"));
			int transactionPOID = query.getInt(query.getColumnIndex("transactionPOID"));
			int sellerCategoryPOID = query.getInt(query.getColumnIndex("sellerCategoryPOID"));
			int buyerAccountPOID = query.getInt(query.getColumnIndex("buyerAccountPOID"));
			long tradeTime = query.getLong(query.getColumnIndex("tradeTime"));
			double buyerMoney = query.getDouble(query.getColumnIndex("buyerMoney"));
			String memo = query.getString(query.getColumnIndex("memo"));
			categoryInfo = categoryMap.get(sellerCategoryPOID);
			int imgResId = categoryInfo.getImgResId();							//转化后的图片资源id
			String name = categoryInfo.getName();
			infoList.add(new TransactionInfo(type, transactionPOID, sellerCategoryPOID, buyerAccountPOID, tradeTime, buyerMoney, memo, imgResId, name));
			toFirst = query.moveToPrevious();
//			toFirst = query.moveToNext();
		}
		query.close();
		return infoList;
	}

	@Override
	public String toString() {
		return "TransactionInfo [type=" + type + ", transactionPOID="
				+ transactionPOID + ", sellerCategoryPOID="
				+ sellerCategoryPOID + ", buyerAccountPOID=" + buyerAccountPOID
				+ ", tradeTime=" + tradeTime + ", buyerMoney=" + buyerMoney
				+ ", memo=" + memo + ", imgResId=" + imgResId + ", name="
				+ name + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//需要信息有
//		int type, int transactionPOID,int sellerCategoryPOID, int buyerAccountPOID,
//		long tradeTime,double buyerMoney, String memo
		dest.writeInt(type);
		dest.writeInt(transactionPOID);
		dest.writeInt(sellerCategoryPOID);
		dest.writeInt(buyerAccountPOID);
		dest.writeLong(tradeTime);
		dest.writeDouble(buyerMoney);
		dest.writeString(memo);
	}
	public static final Parcelable.Creator<TransactionInfo> CREATOR = new Creator<TransactionInfo>() {
		
		@Override
		public TransactionInfo[] newArray(int size) {
			return new TransactionInfo[size];
		}
		
		@Override
		public TransactionInfo createFromParcel(Parcel source) {
			return new TransactionInfo(source.readInt(), source.readInt(), source.readInt(), source.readInt(), 
					source.readLong(), source.readDouble(), source.readString());
		}
	};
	
}
