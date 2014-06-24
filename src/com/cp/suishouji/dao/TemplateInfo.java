package com.cp.suishouji.dao;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

import com.cp.suishouji.utils.DataBaseUtil;

public class TemplateInfo implements Parcelable{
	public String templateName;
	public int type;										//交易类型,支出,收入	
	public int transactionPOID;					//交易ID
	public int sellerCategoryPOID;			//category类型id  			表t_category(图片,标题)
	public int buyerAccountPOID;						//账户 								表t_account	
	public double buyerMoney;				//2位小数
	public String memo;								//备注
	
	public int imgResId;							//转化后的图片资源id
	public String name;								//category名
	
	
	public TemplateInfo(String templateName, int type, int transactionPOID,
			int sellerCategoryPOID, int buyerAccountPOID, double buyerMoney,
			String memo, int imgResId, String name) {
		super();
		this.templateName = templateName;
		this.type = type;
		this.transactionPOID = transactionPOID;
		this.sellerCategoryPOID = sellerCategoryPOID;
		this.buyerAccountPOID = buyerAccountPOID;
		this.buyerMoney = buyerMoney;
		this.memo = memo;
		this.imgResId = imgResId;
		this.name = name;
	}


	public static ArrayList<TemplateInfo> query(Context context) {
		CategoryInfo categoryInfo;
		HashMap<Integer, CategoryInfo> categoryMap = CategoryInfo.getCategoryMap(context);
		if(categoryMap==null){
			Log.e("transactionInfo:static:query", "categoryMap==null");
			return null;
		}
		ArrayList<TemplateInfo> infoList = new ArrayList<TemplateInfo>();
		Cursor query = DataBaseUtil.getDb().query("t_transaction_template", null, null, null, null, null, "transactionTemplatePOID");
		boolean toFirst = query.moveToLast();
		if(!toFirst){
			Log.e("当月没有数据", "xx");
			return null;
		}
		while(toFirst){
			String templateName = query.getString(query.getColumnIndex("name"));
			int type = query.getInt(query.getColumnIndex("type"));
			int transactionPOID = query.getInt(query.getColumnIndex("transactionTemplatePOID"));
			int sellerCategoryPOID = query.getInt(query.getColumnIndex("sellerCategoryPOID"));
			int buyerAccountPOID = query.getInt(query.getColumnIndex("buyerAccountPOID"));
			double buyerMoney = query.getDouble(query.getColumnIndex("buyerMoney"));
			String memo = query.getString(query.getColumnIndex("memo"));
			categoryInfo = categoryMap.get(sellerCategoryPOID);
			int imgResId = categoryInfo.getImgResId();							//转化后的图片资源id
			String name = categoryInfo.getName();
			infoList.add(new TemplateInfo(templateName,type, transactionPOID, sellerCategoryPOID, buyerAccountPOID,  buyerMoney, memo, imgResId, name));
			toFirst = query.moveToPrevious();
		}
		query.close();
		return infoList;
	}


	@Override
	public String toString() {
		return "TemplateInfo [templateName=" + templateName + ", type=" + type
				+ ", transactionPOID=" + transactionPOID
				+ ", sellerCategoryPOID=" + sellerCategoryPOID
				+ ", buyerAccountPOID=" + buyerAccountPOID + ", buyerMoney="
				+ buyerMoney + ", memo=" + memo + ", imgResId=" + imgResId
				+ ", name=" + name + "]";
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
//		public String templateName;
//		public int type;										//交易类型,支出,收入	
//		public int transactionPOID;					//交易ID
//		public int sellerCategoryPOID;			//category类型id  			表t_category(图片,标题)
//		public int buyerAccountPOID;						//账户 								表t_account	
//		public double buyerMoney;				//2位小数
//		public String memo;								//备注
//		
//		public int imgResId;							//转化后的图片资源id
//		public String name;								//category名
		dest.writeString(templateName);
		dest.writeInt(type);
		dest.writeInt(transactionPOID);
		dest.writeInt(sellerCategoryPOID);
		dest.writeInt(buyerAccountPOID);
		dest.writeDouble(buyerMoney);
		dest.writeString(memo);
		dest.writeInt(imgResId);
		dest.writeString(name);
	}
	public static final Parcelable.Creator<TemplateInfo> CREATOR = new Creator<TemplateInfo>() {
		
		@Override
		public TemplateInfo[] newArray(int size) {
			return new TemplateInfo[size];
		}
		
		@Override
		public TemplateInfo createFromParcel(Parcel source) {
			return new TemplateInfo(source.readString(),source.readInt(), source.readInt(), source.readInt(), source.readInt(), 
					 source.readDouble(), source.readString(),source.readInt(),source.readString());
		}
	};
}
