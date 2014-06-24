package com.cp.suishouji.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.EditTempleActivity;
import com.cp.suishouji.KaniuActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.R.id;
import com.cp.suishouji.R.layout;
import com.cp.suishouji.adapter.TemplateAdapter;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.dao.TemplateInfo;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.zbar.lib.decode.FinishListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加,删除,修改模板
 * 
 */
public class AssistantFragment extends RefreshableFragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private ArrayList<TemplateInfo> templateList;
	private TemplateAdapter mAdapter;
	private View emptyview;
	private ListView listView;
	public AssistantFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_assistant, container, false);
		layout.findViewById(R.id.kaniu).setOnClickListener(this);
		layout.findViewById(R.id.img_add).setOnClickListener(this);
		initListView(layout);
		return layout;
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.listView1);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		emptyview = layout.findViewById(R.id.emptyview);
		templateList = TemplateInfo.query(getActivity());
		if(templateList==null){
			//显示还没有记账模板
			emptyview.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}else{
			listView.setVisibility(View.VISIBLE);
			emptyview.setVisibility(View.GONE);
			mAdapter = new TemplateAdapter(getActivity(), templateList,0);
			listView.setAdapter(mAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.kaniu:
			intent = new Intent(getActivity(),KaniuActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.img_add:
			intent = new Intent(getActivity(),EditTempleActivity.class);
			startActivityForResult(intent, 0);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TemplateInfo info = mAdapter.getItem(position);
		TransactionInfo transactionInfo = new TransactionInfo(info.type, info.transactionPOID, info.sellerCategoryPOID, info.buyerAccountPOID, System.currentTimeMillis(),
				info.buyerMoney, info.memo);
		Intent intent = new Intent(getActivity(),AddOrEditTransActivity.class);
		intent.putExtra("mode", 2);
		Bundle bundle = new Bundle();
		bundle.putParcelable("transactioninfo", transactionInfo);
		intent.putExtras(bundle);
		startActivity(intent);
		getActivity().finish();
	}
	//修改,删除了模板后,需要更新界面
	public void refresh(){
		templateList = TemplateInfo.query(getActivity());
		if(templateList==null){
			//显示还没有记账模板
			emptyview.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}else{
			listView.setVisibility(View.VISIBLE);
			emptyview.setVisibility(View.GONE);
			mAdapter = new TemplateAdapter(getActivity(), templateList,0);
			listView.setAdapter(mAdapter);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_assistant, null);
		alertDialog.show();
		alertDialog.setContentView(dialogView);
		DialogListener dialogListener = new DialogListener(alertDialog, position);
		dialogView.findViewById(R.id.tv_edit).setOnClickListener(dialogListener);
		dialogView.findViewById(R.id.tv_delete).setOnClickListener(dialogListener);
		dialogView.findViewById(R.id.btn_cancel).setOnClickListener(dialogListener);
		return true;
	}
	
	/**
	 * 对话框时间
	 * @author cp
	 *
	 */
	class DialogListener implements OnClickListener{
		AlertDialog alertDialog;
		int position;
		
		public DialogListener(AlertDialog alertDialog, int position) {
			super();
			this.alertDialog = alertDialog;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_edit:
				Intent intent = new Intent(getActivity(),EditTempleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("templateInfo", templateList.get(position));
				intent.putExtras(bundle);
				intent.putExtra("mode", 1);
				startActivityForResult(intent, 0);
				break;
			case R.id.tv_delete:
				DataBaseUtil.getDb().delete("t_transaction_template", "transactionTemplatePOID=?", new String[]{String.valueOf(templateList.get(position).transactionPOID)});
				templateList.remove(position);
				mAdapter.notifyDataSetChanged();
				if(templateList.size()==0){
					emptyview.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}
				Toast.makeText(getActivity(), "删除成功!", 0).show();
				
				break;
			case R.id.btn_cancel:
//				alertDialog.dismiss();
				break;

			default:
				break;
			}
			alertDialog.dismiss();
		}}
}
