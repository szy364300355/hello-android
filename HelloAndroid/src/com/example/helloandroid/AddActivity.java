package com.example.helloandroid;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity  extends Activity{
	private Button btnBack;	private Button btnSave;
	private EditText editText;
	private TextView dateText;
	private EditText titleText;
	String date;
	private String id;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_layout);
		
		this.id=getIntent().getExtras().getString("id");
		
		btnBack=(Button)this.findViewById(R.id.btnAddBack);
		btnBack.setOnClickListener(new MyListener());
		btnSave=(Button)this.findViewById(R.id.btnAddSave);
		btnSave.setOnClickListener(new MyListener());
		
		editText=(EditText) this.findViewById(R.id.contentAddView);
		dateText=(TextView)this.findViewById(R.id.addDateText);
		titleText=(EditText) this.findViewById(R.id.addTitle);
		
		Date dt=new Date(); 
		SimpleDateFormat matter=new SimpleDateFormat("yyyy/MM/dd");
		date=matter.format(dt);
		dateText.setText(date);
	}
	private class MyListener  implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==btnBack){
				finish();
			}
			if(v==btnSave){
				String tmp=titleText.getText().toString().trim();
				if(tmp.equals(""))
					{
					 Toast toast = Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					return;
					}
				
				tmp=editText.getText().toString();
				String brief=tmp;
				if(tmp.length()>40){
					brief=tmp.substring(0, 39);
				}
				
				HelloActivity.userDao.insertData(id, brief, tmp,titleText.getText().toString());
				finish();
			}
		}
		
	}
}
