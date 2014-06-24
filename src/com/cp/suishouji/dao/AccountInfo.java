package com.cp.suishouji.dao;
/**
 * 账户信息,作为给Adapter显示用,不是完全根据数据库field取成员变量
 * @author Administrator
 *	ex:
 *	表"t_account"				:支付宝 accountPOID = -6,accountGroupPOID = 9;
 * 表"t_account_group"  :虚拟账户 accountGroupPOID = 8,parentAccountGroupPOID = 1;
 * 										 在线支付 accountGroupPOID = 9,parentAccountGroupPOID = 8;
 */
public class AccountInfo implements Comparable<AccountInfo> {
	public long accountPOID;//t_account 中
	public long accountGroupPOID;//
	public long parentAccountGroupPOID;//是否一级分类,=1时为根
	public String name;				//名字
	public String secgroupname;				//二级分类名字
	public int type;
	public double balance;//收入-支出
	public double amountOfLiability;//负债
	public double amountOfCredit;//债权
	@Override
	public int compareTo(AccountInfo another) {
		return (int)(this.accountGroupPOID - another.accountGroupPOID);
	}
	@Override
	public String toString() {
		return "AccountInfo [accountPOID=" + accountPOID
				+ ", accountGroupPOID=" + accountGroupPOID
				+ ", parentAccountGroupPOID=" + parentAccountGroupPOID
				+ ", name=" + name + ", secgroupname=" + secgroupname
				+ ", type=" + type + ", balance=" + balance
				+ ", amountOfLiability=" + amountOfLiability
				+ ", amountOfCredit=" + amountOfCredit + "]";
	}
	
}
