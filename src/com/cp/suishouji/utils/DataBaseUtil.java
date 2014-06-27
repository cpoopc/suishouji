package com.cp.suishouji.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.AccountBookInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


/**
 * 1.copy数据库到apk包
 * 2.获取database
 * @author NGJ
 * 
 */
public class DataBaseUtil {

	private Context context;
	public static String dbName = "0示例账本.db";// 预置数据库的名字
//	public static String dbName = "mymoney.db";// 数据库的名字
	private static String DATABASE_PATH;// 数据库在手机里的路径
	private static SQLiteDatabase database;
	public DataBaseUtil(Context context) {
		this.context = context;
		String packageName = context.getPackageName();
		DATABASE_PATH="/data/data/"+packageName+"/databases/";
	}

	/**
	 * 判断数据库是否存在
	 * 
	 * @return false or true
	 */
	public boolean checkDataBase() {
		SQLiteDatabase db = null;
		try {
			String databaseFilename = DATABASE_PATH + dbName;
			db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {

		}
		if (db != null) {
			db.close();
		}
		return db != null ? true : false;
	}

	/**
	 * 复制数据库到手机指定文件夹下
	 * 
	 * @throws IOException
	 */
	public void copyDataBase(int rawID,String dbname) throws IOException {
		String databaseFilenames = DATABASE_PATH + dbname;
		File dir = new File(DATABASE_PATH);
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdir();
		FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
		InputStream is = context.getResources().openRawResource(rawID);// 得到数据库文件的数据流 
		byte[] buffer = new byte[8192];
		int count = 0;
		while ((count = is.read(buffer)) > 0) {
			os.write(buffer, 0, count);
			os.flush();
		}
		is.close();
		os.close();
	}
	//删除账本
	public void removeDb(String bookName){
		String databaseFilenames = DATABASE_PATH + bookName+".db";
		File dir = new File(databaseFilenames);
		if(dir.exists()){
			dir.delete();
		}
	}
	//新建账本
	public void creatDb(String bookName) throws IOException{
		
		String databaseFilenames = DATABASE_PATH + bookName+".db";
		File dir = new File(DATABASE_PATH);
		if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
			dir.mkdir();
		FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
		InputStream is = context.getResources().openRawResource(R.raw.template);// 得到数据库文件的数据流 
		byte[] buffer = new byte[8192];
		int count = 0;
		while ((count = is.read(buffer)) > 0) {
			os.write(buffer, 0, count);
			os.flush();
		}
		is.close();
		os.close();
		
	}
	/**
	 * 分局账本设置当前数据库
	 * @param bookName
	 */
	public static void setCurBook(String bookName){
		dbName = bookName + ".db";
	}
	//获得账本db
	public static void initAccountBookDb(long clientID){
		if(clientID==0){
			dbName = "0示例账本.db";
			return;
		}
		SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH+"accountbook.db", null, SQLiteDatabase.OPEN_READWRITE);
//		db.query("t_account_book", table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor query = db.query("t_account_book", null, "clientID=?", new String[]{String.valueOf(clientID)}, null, null, null);
		query.moveToFirst();
		String name = query.getString(query.getColumnIndex("name"));
		dbName = clientID+name+".db";
		db.close();
	}
	public static SQLiteDatabase getBookDb(){
		return SQLiteDatabase.openDatabase(DATABASE_PATH+"accountbook.db", null, SQLiteDatabase.OPEN_READWRITE);
	}
	//如何安全关闭数据库?
	public static SQLiteDatabase getDb(){
		if(database==null){
			synchronized (DATABASE_PATH) {
				if(database==null){
					database = SQLiteDatabase.openDatabase(DATABASE_PATH+dbName, null, SQLiteDatabase.OPEN_READWRITE);
				}
			}
		}
		
		return database;
	}
	public static void closeDb(){
		if(database!=null){
			database.close();
			database = null;
		}
	}
	/**
	 * 更新账户
	 * @param buyerMoney	支出为负,收入为正
	 */
	public static  void modify(Double buyerMoney,long accountPOID) {
		SQLiteDatabase db = DataBaseUtil.getDb();
		Cursor query = db.query("t_account", null, "accountPOID=?", new String[]{String.valueOf(accountPOID)}, null, null, null);
		if(query.moveToFirst()){
			double balance = query.getDouble(query.getColumnIndex("balance"));
			double amountOfLiability = query.getDouble(query.getColumnIndex("amountOfLiability"));
			double amountOfCredit = query.getDouble(query.getColumnIndex("amountOfCredit"));
			long accountGroupPOID = query.getLong(query.getColumnIndex("accountGroupPOID"));
			Cursor query_group = db.query("t_account_group", null, "accountGroupPOID=?", new String[]{String.valueOf(accountGroupPOID)},
					null, null, null);
			if(query_group.moveToFirst()){
				//账户类型:资产录入balance,负债录入amountOfLiability,信用录入amountOfCredit
				int type = query_group.getInt(query_group.getColumnIndex("type"));
				switch (type) {
				case 0:
					balance += buyerMoney;
					break;
				case 1:
					amountOfLiability -= buyerMoney;
					break;
				case 2:
					amountOfCredit -= buyerMoney;
					break;

				default:
					break;
				}
			}
			ContentValues values2 = new ContentValues();
			values2.put("balance", balance);
			values2.put("amountOfLiability", amountOfLiability);
			values2.put("amountOfCredit", amountOfCredit);
			db.update("t_account", values2, "accountPOID=?", new String[]{String.valueOf(accountPOID)});
		}
	}
}
