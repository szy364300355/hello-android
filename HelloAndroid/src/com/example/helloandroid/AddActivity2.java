package com.example.helloandroid;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.helloandroid.utils.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class AddActivity2  extends Activity implements OnCheckedChangeListener{
	public final static int  ADD=1;
	public final static int  EDIT=2;
private int tag=1;
private String[] title={"DETAILS","PICTURE"};
private Button btnBack;
private Button btnAdd;
private RadioButton btn1;
private RadioButton btn2;
private RadioGroup radioGroup;
private ViewPager page;//横向滑动控件
private ArrayList<View> mViews;//存放滚动layout（Details，Picture）
private ImageView imgv;
private TextView textView;
private boolean hasPicture=false;
private EditText editText;
private TextView dateText;
private EditText titleText;
String date;
private String id;
private Bitmap tmpPicture=null;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_layout_main);
	this.id=getIntent().getExtras().getString("id");
	btnBack=(Button) this.findViewById(R.id.btnAddBack);
	btnAdd=(Button) this.findViewById(R.id.btnAddSave);
	btn1=(RadioButton) this.findViewById(R.id.btn1);
	btn2=(RadioButton) this.findViewById(R.id.btn2);
	radioGroup=(RadioGroup)this.findViewById(R.id.radioGroup);
	btn1.setChecked(true);
	init();
	initListener();
	page.setCurrentItem(0);
}

private void initListener(){
	
	radioGroup.setOnCheckedChangeListener(this);
	page.setOnPageChangeListener(new MyPagerOnPageChangeListener());
	btnBack.setOnClickListener(new MyListener());
	btnAdd.setOnClickListener(new MyListener());
}
private void init(){
	Date dt=new Date(); 
	SimpleDateFormat matter=new SimpleDateFormat("yyyy/MM/dd");
	date=matter.format(dt);
	if(tag==1)
	{
	mViews=new ArrayList<View>();
	mViews.add(getLayoutInflater().inflate(R.layout.add_layout2, null));
	mViews.add(getLayoutInflater().inflate(R.layout.picture_layout, null));
	textView=(TextView)mViews.get(1).findViewById(R.id.pictureLayoutTextView);
		imgv=(ImageView) mViews.get(1).findViewById(R.id.picture);
		imgv.setVisibility(View.INVISIBLE);
		
		
		mViews.get(1).setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				
				
				CharSequence[] items=null;
				if(!imgv.isShown()){
					items=new CharSequence[]{new String("拍一张照片"),new String("从相册中选一张照片")};
				}
				if(imgv.isShown()){
					items=new CharSequence[]{new String("拍一张照片替换"),new String("从相册中选一张替换"),new String("删除这张图片")};
				}
				new AlertDialog.Builder(AddActivity2.this).setTitle("图片操作")
				.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if(which==0)
						{
							//TODO 调用相机
							Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent,1);
						}
						else if(which==1){
							//TODO 调用相册或是从相册取数据
							InputStream is=null;
							try {
								is = getResources().getAssets().open("test.png");
							} catch (IOException e) {
								e.printStackTrace();
							}
							AddActivity2.this.tmpPicture=BitmapFactory.decodeStream(is);
							showPicture(true);
							
							try {
								is.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else if(which==2){
							showPicture(false);
						}
					}
				}).show();
				
				return true;
			}
			
		});
	}
	page=(ViewPager) this.findViewById(R.id.pager);
	page.setAdapter(new MyPagerAdapter());//设置自定义适配器
	
	editText=(EditText)mViews.get(0).findViewById(R.id.contentAddView);
	titleText=(EditText) this.findViewById(R.id.addTitle);
//	page.setVisibility(View.INVISIBLE);

}

private class MyPagerAdapter extends PagerAdapter{
	@Override
	public void destroyItem(View v,int position,Object obj){
		((ViewPager)v).removeView(mViews.get(position));
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(View v,int position){
		((ViewPager)v).addView(mViews.get(position));
		return mViews.get(position);
	}
	
}
/**
 *页面改变的监听器 
 */
private class MyPagerOnPageChangeListener implements OnPageChangeListener{

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		if(arg0==0){
		 btn1.performClick();
		}else if(arg0==1){
		btn2.performClick();
//	     btn1.setChecked(false);
//	     btn2.setChecked(true);
		}
	}
	
}
@Override
public void onCheckedChanged(RadioGroup group, int checkedId) {
	if(checkedId==R.id.btn1){
		page.setCurrentItem(0);
	}else if(checkedId==R.id.btn2){
		page.setCurrentItem(1);
	}
	
}
@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
	super.onActivityResult(requestCode, resultCode, data);
	if(resultCode==Activity.RESULT_OK){
		Bundle bundle=data.getExtras();
		tmpPicture=(Bitmap) bundle.get("data");
		tmpPicture=Utils.initPicture(tmpPicture,AddActivity2.this);
		showPicture(true);
	}
}
private void showPicture(boolean isShow){
	if(isShow){
		textView.setVisibility(View.INVISIBLE);
		imgv.setImageBitmap(tmpPicture);
		imgv.setVisibility(View.VISIBLE);
		hasPicture=true;
	}else{
		AddActivity2.this.tmpPicture=null;
		textView.setVisibility(View.VISIBLE);
		imgv.setImageBitmap(null);
		imgv.setVisibility(View.INVISIBLE);
		hasPicture=false;
	}
}
private class MyListener  implements OnClickListener{

	@Override
	public void onClick(View v) {
		if(v==btnBack){
			finish();
		}
		if(v==btnAdd){
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
			if(hasPicture){
				HelloActivity.userDao.insertDataWithPicture(id, brief, tmp,titleText.getText().toString(),tmpPicture);
			}else{
			HelloActivity.userDao.insertData(id, brief, tmp,titleText.getText().toString());
			}
			finish();
		}
	}
	
}
}

