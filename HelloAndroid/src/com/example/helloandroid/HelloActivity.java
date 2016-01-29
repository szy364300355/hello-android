package com.example.helloandroid;

import com.example.helloandroid.database.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
/**
 * 主Activity 实现登录页面
 * @author szy
 *
 */
public class HelloActivity extends Activity {
	 private Button btnLogin;
	 private EditText account=null;
	 private EditText pass=null;
	 private Button btnFinish;
	 private Button btnSign;
	 public static UserDao userDao;
	 int cont=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_pass);      
        
        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new MyListener());
        btnFinish=(Button)this.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new MyListener());
        btnSign=(Button)this.findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new MyListener());
        account=(EditText)this.findViewById(R.id.editBoxAccount);
        pass=(EditText)this.findViewById(R.id.editBoxPassword);
        
//初始化数据库 清空里面所有数据
        userDao=new UserDao(getBaseContext());
        userDao.createAll();
        init();
        for(int i=0;i<16;i++)
        { 
        userDao.insertData(1+"", "test brief"+cont, "test brief"+cont,"test title"+cont);
		 cont++;
		 }
    }
    /**
     * 初始化数据库
     */
    public void init(){
    	userDao.delectAll("userdata");
        userDao.delectAll("user");
        userDao.insertUser("test", "test");
		 account.setText("test");
		 pass.setText("test");
    }
    @Override
    protected void onResume(){
    	super.onResume();

    }
/**
 * 为登录按钮添加监听器
 */
private class MyListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		if(v==btnLogin){
			//用户名或者密码为空
			if(account.getText().toString().trim().equals("")||pass.getText().toString().trim().equals(""))
			{
				Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_LONG);
				 toast.setGravity(Gravity.CENTER, 0, 0);
				 toast.show();
				 return;
			}
			
			//校验用户名密码
			 if(!userDao.checkUserAndPass(account.getText().toString(), pass.getText().toString()))
			 {
				 Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG);
				 toast.setGravity(Gravity.CENTER, 0, 0);
				 toast.show();
				 return;
			 }
			 
			 Bundle bundle=new Bundle(); 
			 bundle.putString("account", account.getText().toString());
			 bundle.putString("pass", pass.getText().toString());
			 
			 //获取用户id
			 String id=String.valueOf(userDao.getRowId(account.getText().toString()));
			 Log.v("HelloActivity", "get userid " + id );
			 bundle.putString("id", id);

			 
		    Intent intent = new Intent(); 
	        intent.setClass(HelloActivity.this, BriefActivity.class);
	        intent.putExtras(bundle);
	        HelloActivity.this.startActivity(intent);
		}
		if(v==btnFinish){
			finish();
		}
		if(v==btnSign){
			//跳转到注册页面
			 Intent intent = new Intent(); 
		     intent.setClass(HelloActivity.this, HelloActivitySign.class);
		     startActivityForResult(intent,1);
		}

	}	
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
