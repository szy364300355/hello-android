package com.example.helloandroid;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity{
	private String id;
	private String rowid;
	private EditActivity.Item content;
	private LinearLayout contentLayout;
	private Button btnBack;
	private Button btnEdit;
	private Button btnSave;
	private Button btnBack2;
	private EditText editText;
	private EditText editTitle;
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
		btnBack2=(Button)this.findViewById(R.id.btnEditBack2);
		btnBack2.setOnClickListener(new MyListener());
		
		content=HelloActivity.userDao.getContent(rowid);
		editText=(EditText) this.findViewById(R.id.contentEditView01);
		editText.setEnabled(false);
		editTitle=(EditText) this.findViewById(R.id.addTitle);
		editTitle.setEnabled(false);
		tv=(TextView)this.findViewById(R.id.editDateText);
		setFrontBotton(true);	
	}
	@Override
    protected void onResume(){
		super.onResume();
		editText.setText(content.content);			
		editTitle.setText(content.title);
		tv.setText(content.date);
	}
	private void setFrontBotton(boolean flag){
		if(!flag){			
			findViewById(R.id.editFront).setVisibility(View.GONE);
			findViewById(R.id.editGone).setVisibility(View.VISIBLE);		
		}else{
			findViewById(R.id.editGone).setVisibility(View.GONE);
			findViewById(R.id.editFront).setVisibility(View.VISIBLE);		
		}
	}
	private class MyListener  implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==btnBack){
				finish();
			}
			if(v==btnEdit){
				editText.setEnabled(true);
				editTitle.setEnabled(true);
				setFrontBotton(false);
			}
			if(v==btnSave){
				if(editTitle.getText().toString().trim().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					 editText.setText(content.content);
					 return;
				}
				editText.setEnabled(false);
				editTitle.setEnabled(false);
				Date dt=new Date(); 
				SimpleDateFormat matter=new SimpleDateFormat("yyyy/MM/dd");
				content.content=editText.getText().toString();
				content.title=editTitle.getText().toString();
				content.date=matter.format(dt);
				HelloActivity.userDao.saveData(id, rowid, content);
				editText.setText(content.content);			
				tv.setText(content.date);
				setFrontBotton(true);
			}
			if(v==btnBack2){
				editText.setText(content.content);	
				editText.setEnabled(false);
				editTitle.setEnabled(false);
				setFrontBotton(true);
			}
		}
		
	}
	public static class Item{
		public String title;
		public String content;
		public String date;
	}
}
