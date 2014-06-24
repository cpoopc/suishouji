package com.cp.suishouji.fragment;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.BudgetSecActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.R.layout;
import com.cp.suishouji.utils.MyCalculator;
import com.cp.suishouji.utils.MyUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CalculateSimpleFragment extends BaseFragment implements OnClickListener {

	private MyCalculator myCalculator;
	private boolean nolyresultmode;
	public CalculateSimpleFragment() {
	}

	public CalculateSimpleFragment(TextView tv_calcuresult) {
		super();
		this.tv_calcuresult = tv_calcuresult;
		nolyresultmode = true; 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calculate_simple, container,false);
		if(nolyresultmode){
			view.findViewById(R.id.ly_result).setVisibility(View.GONE);
		}else{
			tv_calcuresult = (TextView) view.findViewById(R.id.tv_result);
		}
		myCalculator = new MyCalculator(tv_calcuresult);
		View layout = view.findViewById(R.id.include1);
		layout.findViewById(R.id.button0).setOnClickListener(this);
		layout.findViewById(R.id.button1).setOnClickListener(this);
		layout.findViewById(R.id.button2).setOnClickListener(this);
		layout.findViewById(R.id.button3).setOnClickListener(this);
		layout.findViewById(R.id.button4).setOnClickListener(this);
		layout.findViewById(R.id.button5).setOnClickListener(this);
		layout.findViewById(R.id.button6).setOnClickListener(this);
		layout.findViewById(R.id.button7).setOnClickListener(this);
		layout.findViewById(R.id.button8).setOnClickListener(this);
		layout.findViewById(R.id.button9).setOnClickListener(this);
		layout.findViewById(R.id.buttonc).setOnClickListener(this);
		layout.findViewById(R.id.buttondot).setOnClickListener(this);
		layout.findViewById(R.id.buttonadd).setOnClickListener(this);
		layout.findViewById(R.id.buttondec).setOnClickListener(this);
		btn_ok = (Button) layout.findViewById(R.id.buttonok);
		btn_ok.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button0:
			myCalculator.pressNum("0");
			break;
		case R.id.button1:
			myCalculator.pressNum("1");
			break;
		case R.id.button2:
			myCalculator.pressNum("2");
			break;
		case R.id.button3:
			myCalculator.pressNum("3");
			break;
		case R.id.button4:
			myCalculator.pressNum("4");
			break;
		case R.id.button5:
			myCalculator.pressNum("5");
			break;
		case R.id.button6:
			myCalculator.pressNum("6");
			break;
		case R.id.button7:
			myCalculator.pressNum("7");
			break;
		case R.id.button8:
			myCalculator.pressNum("8");
			break;
		case R.id.button9:
			myCalculator.pressNum("9");
			break;
		case R.id.buttonc:
			myCalculator.pressClear();
			break;
		case R.id.buttondot:
			myCalculator.pressDot();
			break;
		case R.id.buttonadd:
			myCalculator.count(ADD);
			break;
		case R.id.buttondec:
			myCalculator.count(DEC);
			break;
		case R.id.buttonok:
			if("=".equals(btn_ok.getText().toString()))
			myCalculator.count();
			break;
			
		default:
			break;
		}
		//设置ok键状态
		switch (v.getId()) {

		case R.id.buttonadd:
		case R.id.buttondec:
			btn_ok.setText("=");
			break;
		
		case R.id.buttonok:
			if("=".equals(btn_ok.getText().toString())){
				btn_ok.setText("OK");
			}else{
				//TODO 关闭界面,保存值,调用activity方法
				if(getActivity() instanceof BudgetSecActivity){
					Log.e("CalculateSimpleFragment", "save:"+tv_calcuresult.getText()+";"+myCalculator.getResult());
					((BudgetSecActivity)getActivity()).save(MyUtil.String2double(tv_calcuresult.getText().toString()));
				}
			}
			break;

		default:
			break;
		}
	}
	/**
	 * 计算器
	 */
	double result;//暂存结果
	int ADD = 1;//加法
	int DEC = 2;//减法
	private TextView tv_calcuresult;
	private Button btn_ok;
	public void setResult(double result){
		this.result = result;
		myCalculator.clear();
		myCalculator.setResult(result);
		tv_calcuresult.setText(MyUtil.doubleFormate(result));
		btn_ok.setText("OK");
	}

	@Override
	public void btnok() {
		// TODO Auto-generated method stub
		
	}
}
