package com.cp.suishouji.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpenseInfo implements Parcelable,Comparable<ExpenseInfo>{
	public String name;
	public long sellerCategoryPOID;//支出种类
	public double buyermoney;
	public ExpenseInfo(long sellerCategoryPOID, double buyermoney) {
		super();
		this.sellerCategoryPOID = sellerCategoryPOID;
		this.buyermoney = buyermoney;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.sellerCategoryPOID);
		dest.writeDouble(this.buyermoney);
	}
	public static final Parcelable.Creator<ExpenseInfo> CREATOR = new Creator<ExpenseInfo>() {
		
		@Override
		public ExpenseInfo[] newArray(int size) {
			return new ExpenseInfo[size];
		}
		
		@Override
		public ExpenseInfo createFromParcel(Parcel source) {
			return new ExpenseInfo(source.readLong(), source.readDouble());
		}
	};
	@Override
	public String toString() {
		return "ExpenseInfo [sellerCategoryPOID=" + sellerCategoryPOID
				+ ", buyermoney=" + buyermoney + "]";
	}
	@Override
	public int compareTo(ExpenseInfo another) {
		return (int) (another.sellerCategoryPOID - this.sellerCategoryPOID);
	}
	
}
