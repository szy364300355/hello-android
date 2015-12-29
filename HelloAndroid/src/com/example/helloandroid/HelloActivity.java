package com.example.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_pass);      

        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new MyListener());
        btnFinish=(Button)this.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new MyListener());
        account=(EditText)this.findViewById(R.id.editBoxAccount);
        pass=(EditText)this.findViewById(R.id.editBoxPassword);
        Log.i("INFO","A on create");
    }
    protected void onResume(Bundle savedInstanceState){
    	Log.i("INFO","A on resume");
    }
    
    
/**
 * 为登录按钮添加监听器
 */
private class MyListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		if(v==btnLogin){
			 Bundle bundle=new Bundle(); 
			 bundle.putString("account", account.getText().toString());
			 bundle.putString("pass", pass.getText().toString());
		    Intent intent = new Intent(); 
	        intent.setClass(HelloActivity.this, HelloActivityB.class);
	        intent.putExtras(bundle);
	        HelloActivity.this.startActivity(intent);
		}
		if(v==btnFinish){
			finish();
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
