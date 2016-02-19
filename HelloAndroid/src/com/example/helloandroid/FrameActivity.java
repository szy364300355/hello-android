package com.example.helloandroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class FrameActivity extends Activity{
	private boolean showFrag1=true;
	public SumFragment fragment1;
	public ImageFragment fragment2;
	private FragmentManager fragmentManager;
	private FragmentTransaction transation;
//	private int clickItem=-1;
	private Button btnBack;
	private Button btnSelect;
	private Button btnBack2;
	private Button btnOk;
	private boolean firstInit=true;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_main_layout);
		btnBack=(Button) this.findViewById(R.id.btnBriefBack);
		btnSelect=(Button) this.findViewById(R.id.btnFrameSelect);
		btnBack2=(Button) this.findViewById(R.id.btnRepeal);
		btnOk=(Button) this.findViewById(R.id.btnYes);
		
		btnSelect.setText("SELETE");
		btnOk.setText("SELETE");
		btnBack2.setText("BACK");
		
		MyListener listener=new MyListener();
		btnBack.setOnClickListener(listener);
		btnSelect.setOnClickListener(listener);
		btnBack2.setOnClickListener(listener);
		btnOk.setOnClickListener(listener);
		
		//创建碎片
		fragment2=new ImageFragment();
		fragment1=new SumFragment();
		//获取布局管理器
		fragmentManager = getFragmentManager();
		//获得碎片事务
		transation=fragmentManager.beginTransaction();
		//替换布局中的碎片
		transation.add(R.id.frame_fragment,fragment1);
		transation.commit();

		showFrag1(true);
		
		
	}
	
	@Override
    protected void onDestroy(){
		
		super.onDestroy();
		((SumFragment) fragment1).close();

	}
	private class MyListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			if(v==btnBack){
				FrameActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
			if(v==btnSelect){
				if(fragment1.selectPosition==-1)
					return;
				Intent data=new Intent();
				data.putExtra("data",fragment1.adapter.getImageString(fragment1.selectPosition));
				FrameActivity.this.setResult(RESULT_OK, data);
//				FrameActivity.this.setResult(RESULT_OK);
				finish();
			}
			if(v==btnBack2){
				//回复页面
				showFrag1(true);
				fragment1.clickItem=-1;
				findViewById(R.id.Front).setVisibility(View.VISIBLE);
				findViewById(R.id.HideLayout).setVisibility(View.GONE);
				
			}
			if(v==btnOk){
				if(fragment1.clickItem==-1)
					return;
				Intent data=new Intent();
				data.putExtra("data",fragment1.adapter.getImageString(fragment1.clickItem));
				FrameActivity.this.setResult(RESULT_OK, data);
//				FrameActivity.this.setResult(RESULT_OK);
				finish();
			}
		}
	}
	public void showFrag1(boolean showFrag1){
		if(showFrag1){
			if(!firstInit){
				findViewById(R.id.HideLayout).setVisibility(View.GONE);
			findViewById(R.id.Front).setVisibility(View.VISIBLE);
				transation=fragmentManager.beginTransaction();
				transation.replace(R.id.frame_fragment, (Fragment)fragment1);
				transation.commit();
			}else{
				this.firstInit=false;

			}
		}else{
			//show Frag2
//			if(clickItem==-1)
//				return;
			findViewById(R.id.HideLayout).setVisibility(View.VISIBLE);
			findViewById(R.id.Front).setVisibility(View.GONE);
			transation=fragmentManager.beginTransaction();
			transation.replace(R.id.frame_fragment, (Fragment)fragment2);
			transation.commit();
			fragment2.btmap=fragment1.getSelectImage();
//			fragment2.showPicture();

		}
	}


}
