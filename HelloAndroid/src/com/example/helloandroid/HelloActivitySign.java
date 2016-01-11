package com.example.helloandroid;



import java.util.regex.Pattern;

import com.example.helloandroid.database.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HelloActivitySign extends Activity {
	 private Button btnNext;
	 private Button btnBack;
	 private EditText account=null;
	 private EditText pass=null;
	 private EditText repass=null;
	 private static final String USERNAME_PATTERN="^[a-zA-Z][a-zA-z0-9_]{3,19}$";
	 private java.util.regex.Pattern pattern=Pattern.compile(USERNAME_PATTERN);
	 private java.util.regex.Matcher matcher;
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
				if(userName.trim().equals("")||password.trim().equals(""))
					{
					 Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					return;
					}
				
				matcher=pattern.matcher(userName);
				if(!matcher.matches()){
					Toast toast = Toast.makeText(getApplicationContext(), "用户名格式错误，请输入4-20位用户名，第一位为字母，其余只能是字母数字及下划线构成", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					return;
				}

				if(password.length()<6||password.length()>20){
					 Toast toast = Toast.makeText(getApplicationContext(), "请输入6-20位密码", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					return;
				}
				if(!password.equals(repeat)){
					 Toast toast = Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					return;
				}
				//数据库唯一性校验
				if(HelloActivity.userDao.ifUserExist(userName))
					{
					 Toast toast = Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					return;
					}
				//插入数据库
				HelloActivity.userDao.insertUser(userName, password);
				finish();
			}
			if(v==btnBack){
				finish();
			}
		}
		
	}
}
