package com.cp.suishouji.adapter;

import com.cp.suishouji.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MySpinnerAdapter implements SpinnerAdapter{
	private Context context;
	private String[] str;
	private int[] imgs = new int[]{R.drawable.icon_pre,R.drawable.icon_next};
	public void setStr(String[] str) {
		this.str = str;
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}public MySpinnerAdapter(Context context, String[] str) {
	super();
	this.context = context;
	this.str = str;
}


	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		if(str==null)
			return 0;
		return str.length + 1;
	}

	@Override
	public String getItem(int position) {
		return str[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null);
		return layout;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View layout;
		
		if(position== 0){
			layout = LayoutInflater.from(context).inflate(R.layout.item_spinner_emppty, null);
//			textView.setVisibility(View.GONE);
		}else{
			layout = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
			TextView textView = (TextView) layout.findViewById(R.id.textView1);
			ImageView imageView = (ImageView) layout.findViewById(R.id.imageView1);
			imageView.setImageResource(imgs[position-1]);
			textView.setText(str[position-1]);
		}
		return layout;
	}

}
