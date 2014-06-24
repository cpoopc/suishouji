package com.cp.suishouji.adapter;

import java.util.ArrayList;

import com.cp.suishouji.AddAccountBookSecActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.dao.AccountBookInfo;
import com.cp.suishouji.utils.DataBaseUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountBookGridAdapter extends AbstractAdapter {
	private Context context;
	private ArrayList<AccountBookInfo> infoList;
	private boolean isEdit;
	public AccountBookGridAdapter(Context context,
			ArrayList<AccountBookInfo> infoList) {
		super();
		this.context = context;
		this.infoList = infoList;
	}
	public void setEdit(boolean edit){
		isEdit = edit;
		notifyDataSetChanged();
	}
	public boolean isEdit(){
		return isEdit;
	}
	@Override
	public int getCount() {
		return infoList.size();
	} 
	@Override
	public AccountBookInfo getItem(int position) {
		return infoList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = LayoutInflater.from(context).inflate(R.layout.item_accountbook, null);
		ImageView img_book = (ImageView) layout.findViewById(R.id.img_book);
		ImageView img_choise = (ImageView) layout.findViewById(R.id.img_choise);
		TextView tv_title = (TextView) layout.findViewById(R.id.tv_bookname);
		AccountBookInfo accountInfo = infoList.get(position);
		int imgID = context.getResources().getIdentifier(accountInfo.getImgName(), "drawable", context.getPackageName());
//		Log.e("accountInfo.getImgName()", accountInfo.getImgName());
		img_book.setImageResource(imgID);
		if(!isEdit){
			if(accountInfo.isChoise()){
				img_choise.setVisibility(View.VISIBLE);
				
			}
		}else{
			//编辑状态
			View operations_ly = layout.findViewById(R.id.operations_ly);
			operations_ly.setVisibility(View.VISIBLE);
			OperationListener listener = new OperationListener(position);
			operations_ly.findViewById(R.id.edit).setOnClickListener(listener);
			operations_ly.findViewById(R.id.pin).setOnClickListener(listener);
			operations_ly.findViewById(R.id.delete).setOnClickListener(listener);
			operations_ly.findViewById(R.id.upgrade).setOnClickListener(listener);
		}
		tv_title.setText(accountInfo.getName());
		return layout;
	}
	class OperationListener implements OnClickListener{
		private int position;
		
		public OperationListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			final AccountBookInfo info = infoList.get(position);
			switch (v.getId()) {
			case R.id.edit:
				Toast.makeText(context, "编辑:"+info.getName(), 0).show();
				Intent intent = new Intent(context,AddAccountBookSecActivity.class);
				intent.putExtra("imgName", info.getImgName());
				intent.putExtra("name", info.getName());
				intent.putExtra("clientID", info.getClintID());
				context.startActivity(intent);
				break;
			case R.id.pin:
				Toast.makeText(context, "发送到桌面:"+info.getName(), 0).show();
				
				break;
			case R.id.delete:
				if(info.isChoise()){
				Toast.makeText(context, "不能删除当前选中账本!", 0).show();
				break;
				}
				new AlertDialog.Builder(context).setTitle("提示")
				.setMessage("确认要删除账本:"+info.getName()+"?")
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SQLiteDatabase db = DataBaseUtil.getDb();
						db.delete("t_account_book", "clientID=?", new String[]{String.valueOf(info.getClintID())});
						Toast.makeText(context, "删除成功:"+info.getName(), 0).show();
						infoList.remove(position);
						notifyDataSetChanged();
					}
				}).create().show();
				break;
			case R.id.upgrade:
				Toast.makeText(context, "升级:"+info.getName(), 0).show();
				
				break;

			default:
				break;
			}
		}
		
	}

}
