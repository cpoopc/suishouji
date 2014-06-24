package com.cp.suishouji.dao;

import java.util.HashMap;

import com.cp.suishouji.utils.DataBaseUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分类信息
 * @author cp
 *
 */
public class CategoryInfo implements Comparable<CategoryInfo>,Parcelable{
	String name;//若唯一,可以作为二级分类的索引
	String _tempIconName;//图片名称
	int categoryPOID;//分类id
	int parentCategoryPOID;//父类id
	int ordered;//用于排序
	int imgResId;//图片资源id
	public double expense;
	public double budget;
	private static HashMap<Integer, CategoryInfo> categoryMap = null;
	public static HashMap<Integer, CategoryInfo>  getCategoryMap(Context context){
		if(categoryMap == null){
			synchronized (CategoryInfo.class) {
					if(categoryMap == null){
						categoryMap = new HashMap<Integer, CategoryInfo>();
					SQLiteDatabase db = DataBaseUtil.getDb();
					Cursor cursor = db.query("t_category", null, null, null, null, null, null);
					boolean toFirst = cursor.moveToFirst();
					while(toFirst){
						String name = cursor.getString(cursor.getColumnIndex("name"));
						String _tempIconName = cursor.getString(cursor.getColumnIndex("_tempIconName"));
						int categoryPOID = cursor.getInt(cursor.getColumnIndex("categoryPOID"));
						int parentCategoryPOID = cursor.getInt(cursor.getColumnIndex("parentCategoryPOID"));
						int ordered = cursor.getInt(cursor.getColumnIndex("ordered"));
						int imgResId = context.getResources().getIdentifier(_tempIconName, "drawable", context.getPackageName());
						categoryMap.put(categoryPOID, new CategoryInfo(name, _tempIconName, categoryPOID, parentCategoryPOID, ordered, imgResId));
						toFirst = cursor.moveToNext();
					}
				}
			}
		}
		return categoryMap;
	}
	@Override
	public int compareTo(CategoryInfo another) {
		return this.ordered - another.ordered;
	}
	
	
	public CategoryInfo(String name, String _tempIconName, int categoryPOID,
			int parentCategoryPOID, double expense, double budget) {
		super();
		this.name = name;
		this._tempIconName = _tempIconName;
		this.categoryPOID = categoryPOID;
		this.parentCategoryPOID = parentCategoryPOID;
		this.expense = expense;
		this.budget = budget;
	}
	public CategoryInfo(String name, String _tempIconName, int categoryPOID,
			int parentCategoryPOID, int ordered, int imgResId) {
		super();
		this.name = name;
		this._tempIconName = _tempIconName;
		this.categoryPOID = categoryPOID;
		this.parentCategoryPOID = parentCategoryPOID;
		this.ordered = ordered;
		this.imgResId = imgResId;
	}
	public CategoryInfo(String name, String _tempIconName, int categoryPOID,
			int parentCategoryPOID, int ordered) {
		super();
		this.name = name;
		this._tempIconName = _tempIconName;
		this.categoryPOID = categoryPOID;
		this.parentCategoryPOID = parentCategoryPOID;
		this.ordered = ordered;
	}
	
	

	
//	@Override
//	public String toString() {
//		return "CategoryInfo [name=" + name + ", _tempIconName="
//				+ _tempIconName + ", categoryPOID=" + categoryPOID
//				+ ", parentCategoryPOID=" + parentCategoryPOID + ", ordered="
//				+ ordered + ", imgResId=" + imgResId + ", expense=" + expense
//				+ ", budget=" + budget + "]";
//	}
	@Override
	public String toString() {
		return "CategoryInfo [name=" + name + "], categoryPOID=" + categoryPOID
				+ ", parentCategoryPOID=" + parentCategoryPOID +  ", expense=" + expense
				+ ", budget=" + budget + "]";
	}
	public int getImgResId() {
		return imgResId;
	}
	public void setImgResId(int imgResId) {
		this.imgResId = imgResId;
	}
	public String getName() {
		return name;
	}

	public String get_tempIconName() {
		return _tempIconName;
	}

	public int getCategoryPOID() {
		return categoryPOID;
	}

	public int getParentCategoryPOID() {
		return parentCategoryPOID;
	}

	public int getOrdered() {
		return ordered;
	}
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this._tempIconName);
		dest.writeInt(this.categoryPOID);
		dest.writeInt(this.parentCategoryPOID);
		dest.writeDouble(this.expense);
		dest.writeDouble(this.budget);
	}
	public static final Parcelable.Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
		
		@Override
		public CategoryInfo[] newArray(int size) {
			return new CategoryInfo[size];
		}
		
		@Override
		public CategoryInfo createFromParcel(Parcel source) {
			return new CategoryInfo(source.readString(), source.readString(), 
					source.readInt(), source.readInt(), source.readDouble(), source.readDouble());
//			return new CategoryInfo(source.readDouble(), source.readDouble());
		}
	};

}
