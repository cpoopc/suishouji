package com.cp.suishouji.adapter;

import java.util.ArrayList;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.SettingItemInfo;
import com.cp.suishouji.widgt.SwitchButton;
import com.cp.suishouji.widgt.SwitchButton.OnChangeListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<SettingItemInfo> infoList;
	private int viewTypeCount;
	@Override
	
	public int getCount() {
		return infoList.size();
	}

	public SettingAdapter(Context context, ArrayList<SettingItemInfo> infoList,int viewTypeCount) {
		super();
		this.context = context;
		this.infoList = infoList;
		this.viewTypeCount = viewTypeCount;
	}
	@Override
	public int getViewTypeCount() {
		return viewTypeCount;
	}
	@Override
	public int getItemViewType(int position) {
		return getItem(position).getType();
	}
	@Override
	public boolean isEnabled(int position) {
		return getItem(position).isClickable();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = null;
		TextView tv_title = null;
		TextView tv_subtitle = null;
		ImageView img_head = null;
		View divider = null;
		SwitchButton switchButton = null;
		SettingItemInfo item = getItem(position);
		switch (getItemViewType(position)) {
		case 0://SINGLE_TEXT
			layout = LayoutInflater.from(context).inflate(R.layout.item_setting_simpletext, null);
			break;
		case 1://IMAGE_TEXT
			layout = LayoutInflater.from(context).inflate(R.layout.item_setting_image_text, null);
			img_head = (ImageView) layout.findViewById(R.id.img_head);
			divider = layout.findViewById(R.id.divider);
			break;
		case 2://IMAGE_2TEXT
			
			break;
		case 3://switchbutton
			layout = LayoutInflater.from(context).inflate(R.layout.item_setting_switchbutton, null);
			img_head = (ImageView) layout.findViewById(R.id.img_head);
			divider = layout.findViewById(R.id.divider);
			switchButton = (SwitchButton) layout.findViewById(R.id.switchButton1);
			switchButton.setOnoff(item.isOn());
			switchButton.setOnChangeListener(new OnChangeListener() {
				
				@Override
				public void onChange(SwitchButton sb, boolean state) {
					Toast.makeText(context, state==true?"开":"关", 0).show();
				}
			});
			break;

		default:
			break;
		}
		tv_title = (TextView) layout.findViewById(R.id.tv_title);
		tv_title.setText(item.getTitle());
		if(tv_subtitle!=null){
			tv_subtitle.setText(item.getSubTitle());
		}
		if(img_head!=null){
			img_head.setImageResource(item.getImageId());
		}
		if(divider!=null){
			if(item.getType()!=0 && position+1<infoList.size()){
				if(getItem(position+1).getType()!=0){
					divider.setVisibility(View.VISIBLE);
				}
			}
		}
		return layout;
	}
	
	@Override
	public SettingItemInfo getItem(int position) {
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
