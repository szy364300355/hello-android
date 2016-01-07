package com.example.helloandroid;



import com.example.helloandroid.database.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class HelloActivitySign extends Activity {
	 private Button btnNext;
	 private Button btnBack;
	 private EditText account=null;
	 private EditText pass=null;
	 private EditText repass=null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_page);
		btnNext=(Button)this.findViewById(R.id.btnSignup);
		btnNext.setOnClickListener(new MyListener());
		btnBack=(Button)this.findViewById(R.id.btnSignback);
		btnBack.setOnClickListener(new MyListener());
		account=(EditText)this.findViewById(R.id.editBoxSignUserName);
		pass=(EditText)this.findViewById(R.id.editBoxSignPass);
		repass=(EditText)this.findViewById(R.id.editBoxRepeatPass);
	}
	private class MyListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==btnNext){
			//注册校验
				String userName=account.getText().toString();
				String password= pass.getText().toString();
				String repeat=repass.getText().toString();
				//输入合法性校验
				if(userName.equals("")||password.equals("")||(!password.equals(repeat)))
					return;
				//数据库唯一性校验
				if(HelloActivity.userDao.ifUserExist(userName))
					return;
				//插入数据库
				HelloActivity.userDao.insertUser(userName, password);
				finish();
//				 Bundle bundle=new Bundle(); 
//				 bundle.putString("account",userName);
//				 bundle.putString("pass",password);
//			    Intent intent = new Intent(); 
//		        intent.setClass(HelloActivitySign.this, HelloActivityB.class);
//		        intent.putExtras(bundle);
//		        HelloActivitySign.this.startActivity(intent);
				
			}
			if(v==btnBack){
				finish();
			}
		}
		
	}
}
