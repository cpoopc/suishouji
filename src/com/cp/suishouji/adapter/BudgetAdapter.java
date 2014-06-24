package com.cp.suishouji.adapter;

import java.util.ArrayList;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class BudgetAdapter extends AbstractAdapter{
	private Context context;
	private ArrayList<CategoryInfo> categoryList;
	private boolean isChild;
	public BudgetAdapter(Context context, ArrayList<CategoryInfo> categoryList ,boolean isChild) {
		super();
		this.context = context;
		this.categoryList = categoryList;
		this.isChild = isChild;
	}

	@Override
	public int getCount() {
		return categoryList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = LayoutInflater.from(context).inflate(R.layout.item_budget_listview, null);
		if(position == 0 && isChild){
			layout.setBackgroundResource(R.drawable.selector_bg_gray);
		}
		TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
		TextView tv_budget = (TextView) layout.findViewById(R.id.tv_budget);
		TextView tv_amount = (TextView) layout.findViewById(R.id.tv_amount);
		TextView tv_progress = (TextView) layout.findViewById(R.id.tv_progress);
		ImageView img_title = (ImageView) layout.findViewById(R.id.img_title);
		CategoryInfo categoryInfo = categoryList.get(position);
		tv_amount.setText("余额:¥ "+MyUtil.doubleFormate(categoryInfo.budget-categoryInfo.expense));
		if((categoryInfo.budget-categoryInfo.expense)<0){
			tv_amount.setTextColor(context.getResources().getColor(R.color.red));
		}
		tv_budget.setText("¥ "+MyUtil.doubleFormate(categoryInfo.budget));
		tv_title.setText(categoryInfo.getName());
		setLength(context, tv_progress, categoryInfo.budget, categoryInfo.expense);
		img_title.setImageResource(context.getResources().getIdentifier(categoryInfo.get_tempIconName(), "drawable", context.getPackageName()));
		return layout;
	}
	//根据预算,消费设置长度
	private void setLength(Context context,TextView tv_progress,double budget,double expense){
		if(budget==0&&expense==0){
			return;
		}
		double total = budget - expense;
		double percent;
		if(total>=0){
			percent = total/budget;
			tv_progress.setBackgroundColor(context.getResources().getColor(R.color.oringe_string));
		}else{
			percent = -total/budget;
			if(percent>1){
				percent = 1;
			}
			tv_progress.setBackgroundColor(context.getResources().getColor(R.color.red));
		}
		LayoutParams params = tv_progress.getLayoutParams();
		params.width = (int) (percent*params.width);
	}
}
