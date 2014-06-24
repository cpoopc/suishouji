package com.cp.suishouji;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cp.suishouji.utils.UmengSherlockFragmentActivity;

public abstract class BaseFragmentActivity extends UmengSherlockFragmentActivity {
	public abstract void setText(int resId,String str,long categoryPOID);
	public abstract void setText(int resId,String str);
	public abstract void setPsStr(String psstr);
	public abstract void setItem(String item);
	public abstract void setAccount(long buyerAccountPOID);
	public abstract void setLocation(long location);
	public abstract void setMember(long member);
}
