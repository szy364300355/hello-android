package com.example.helloandroid;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditActivity extends Activity{
	private String id;
	private String rowid;
	private EditActivity.Item content;
	private LinearLayout contentLayout;
	private Button btnBack;
	private Button btnEdit;
	private Button btnSave;
	private EditText editText;
	private TextView tv;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_layout);
		//获取传入数据 用户id 及在list中的位置 position
		id=getIntent().getExtras().getString("id");
		rowid=getIntent().getExtras().getString("rowid");
		//构造item 填充页面
		
		btnBack=(Button)this.findViewById(R.id.btnEditBack);
		btnBack.setOnClickListener(new MyListener());
		btnEdit=(Button)this.findViewById(R.id.btnEditAdd);
		btnEdit.setOnClickListener(new MyListener());
		btnSave=(Button)this.findViewById(R.id.btnEditSave);
		btnSave.setOnClickListener(new MyListener());
		
		content=HelloActivity.userDao.getContent(rowid);
		editText=(EditText) this.findViewById(R.id.contentEditView01);
		editText.setEnabled(false);
		tv=(TextView)this.findViewById(R.id.editDateText);
	}
	@Override
    protected void onResume(){
		super.onResume();
			
		editText.setText(content.content);			
		tv.setText(content.date);
	}
	private class MyListener  implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==btnBack){
				finish();
			}
			if(v==btnEdit){
				editText.setEnabled(true);
			}
			if(v==btnSave){
				editText.setEnabled(false);
				Date dt=new Date(); 
				SimpleDateFormat matter=new SimpleDateFormat("yyyy/MM/dd");
				content.content=editText.getText().toString();
				content.date=matter.format(dt);
				HelloActivity.userDao.saveData(id, rowid, content);
				editText.setText(content.content);			
				tv.setText(content.date);
			}
		}
		
	}
	public static class Item{
		public String content;
		public String date;
	}
}
