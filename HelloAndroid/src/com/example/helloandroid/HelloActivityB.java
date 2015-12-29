package com.example.helloandroid;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class HelloActivityB extends Activity  {
		private EditText inputFromAccount; 
		private EditText inputFromPass; 
		private Button btnBack;
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_hello);  
	        Log.i("INFO","B on create");
	        onResume(savedInstanceState);
	        btnBack=(Button)this.findViewById(R.id.btnBack);
	        btnBack.setOnClickListener(new MyBackListener());
//	        TextView tv=new TextView(this);
//	        tv.setText("Hello World!");
//	        setContentView(tv);
 
	 }
		private class MyBackListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				finish();
			}
			
		}
		
	    protected void onResume(Bundle savedInstanceState){
	    	Intent intent=getIntent();
	    	inputFromAccount=(EditText) this.findViewById(R.id.inputAccount);
	        String input="Hi "+intent.getExtras().getString("account")+" : ";
	        inputFromAccount.setText(input);
	        inputFromPass=(EditText) this.findViewById(R.id.inputPass);
	        input="Your password is   :  "+intent.getExtras().getString("pass");
	        inputFromPass.setText(input);
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
