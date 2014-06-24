package com.cp.suishouji.dao;
/**
 * 账本信息
 * @author Administrator
 *
 */
public class AccountBookInfo {
	private long clintID;
	private String name;
	private String imgName;
	private boolean isChoise;
//	private boolean isEdit;
	public AccountBookInfo(long clintID, String name, String imgName,boolean isChoise) {
		super();
		this.clintID = clintID;
		this.name = name;
		this.imgName = imgName;
		this.isChoise = isChoise;
	}
	


	public boolean isChoise() {
		return isChoise;
	}

	public void setChoise(boolean isChoise) {
		this.isChoise = isChoise;
	}

	public long getClintID() {
		return clintID;
	}
	public void setClintID(long clintID) {
		this.clintID = clintID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	@Override
	public String toString() {
		return "AccountBookInfo [clintID=" + clintID + ", name=" + name
				+ ", imgName=" + imgName + ", isChoise=" + isChoise + "]";
	}

	
}
