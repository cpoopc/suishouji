package com.cp.suishouji.dao;

public class SettingItemInfo {
	int type;//类型:
	int imageId;
	String title;
	String subTitle;
	String rightText;//extrainfo
	boolean isOn;
	boolean clickable;
	
	
	public static int SINGLE_TEXT = 0;
	public static int IMAGE_TEXT = 1;
	public static int IMAGE_2TEXT = 1;
	/**
	 * 只有一个标题,不可点击
	 * type=0;
	 * @SettingItemInfo.SINGLE_TEXT;
	 * @param title
	 */
	public SettingItemInfo(String title) {
		super();
		this.title = title;
		this.type = 0;
		this.clickable = false;
	}
	/**
	 * 图片加标题
	 * type=1;
	 * @SettingItemInfo.IMAGE_TEXT
	 * @param imageId
	 * @param title
	 */
	public SettingItemInfo(int imageId, String title) {
		super();
		this.imageId = imageId;
		this.title = title;
		this.type = 1;
		this.clickable = true;
	}
	/**
	 * 图片加标题,子标题
	 * type=2;
	 * @SettingItemInfo.IMAGE_2TEXT
	 * @param imageId
	 * @param title
	 * @param subTitle
	 */
	public SettingItemInfo(int imageId, String title, String subTitle) {
		super();
		this.imageId = imageId;
		this.title = title;
		this.subTitle = subTitle;
		this.type = 2;
		this.clickable = true;
	}
	/**
	 * switchbutton
	 * type=3
	 * @param imageId
	 * @param title
	 * @param isCheck
	 */
	public SettingItemInfo(int imageId, String title, boolean isOn) {
		super();
		this.imageId = imageId;
		this.title = title;
		this.isOn = isOn;
		this.type = 3;
		this.clickable = true;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getRightText() {
		return rightText;
	}
	public void setRightText(String rightText) {
		this.rightText = rightText;
	}
	public boolean isOn() {
		return isOn;
	}
	public void setOnoff(boolean onOff) {
		this.isOn = onOff;
	}
	public boolean isClickable() {
		return clickable;
	}
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
}
