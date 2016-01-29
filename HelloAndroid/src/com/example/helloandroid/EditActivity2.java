package com.example.helloandroid;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class EditActivity2 extends Activity implements OnCheckedChangeListener{
	public final static int  ADD=1;
	public final static int  EDIT=2;
	

	
private int tag=1;
private String[] title={"DETAILS","PICTURE"};
//button
private Button btnSave;
private Button btnBack2;
private Button btnBack;
private Button btnEdit;
private RadioButton btn1;
private RadioButton btn2;
private RadioGroup radioGroup;
private ViewPager page;//横向滑动控件
private ArrayList<View> mViews;//存放滚动layout（Details，Picture）
private ImageView imgv;
private TextView textView;
private boolean hasPicture=false;



private EditText editText;
private EditText editTitle;
private TextView dateText;
private TextView tv;
//用户id
private String id;
//item
private EditActivity.Item content;
private String rowid;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_layout_main);
	//获取传入数据 用户id 及在list中的位置 position
	this.id=getIntent().getExtras().getString("id");
	rowid=getIntent().getExtras().getString("rowid");
	hasPicture=getIntent().getExtras().getBoolean("hasPicture");
	content=HelloActivity.userDao.getContent(rowid);
	if(hasPicture)
		content.picture=HelloActivity.userDao.getPicture(rowid);
	init();
	

	
	page.setCurrentItem(0);
}
private void initListener(){
	
	radioGroup.setOnCheckedChangeListener(this);
	page.setOnPageChangeListener(new MyPagerOnPageChangeListener());
	btnBack.setOnClickListener(new MyListener());
	btnEdit.setOnClickListener(new MyListener());
	btnBack2.setOnClickListener(new MyListener());
	btnSave.setOnClickListener(new MyListener());
}
private void init(){
	btnBack=(Button) this.findViewById(R.id.btnAddBack);
	btnEdit=(Button) this.findViewById(R.id.btnAddSave);
	btnBack2=(Button) this.findViewById(R.id.btnEditBack);
	btnSave=(Button) this.findViewById(R.id.btnEditSave);
	btnEdit.setText("EDIT");
	btn1=(RadioButton) this.findViewById(R.id.btn1);
	btn2=(RadioButton) this.findViewById(R.id.btn2);
	radioGroup=(RadioGroup)this.findViewById(R.id.radioGroup);
	btn1.setChecked(true);

	mViews=new ArrayList<View>();
	mViews.add(getLayoutInflater().inflate(R.layout.add_layout2, null));
	mViews.add(getLayoutInflater().inflate(R.layout.picture_layout, null));
	textView=(TextView)mViews.get(1).findViewById(R.id.pictureLayoutTextView);
	textView.setText("你还没有添加图片哟！");
	imgv=(ImageView) mViews.get(1).findViewById(R.id.picture);
		

	
	page=(ViewPager) this.findViewById(R.id.pager);
	page.setAdapter(new MyPagerAdapter());//设置自定义适配器
	
	editText=(EditText)mViews.get(0).findViewById(R.id.contentAddView);
	editTitle=(EditText) this.findViewById(R.id.addTitle);
	
	setEditable(false);
	initListener();
//	page.setVisibility(View.INVISIBLE);
	resetDetailsAndPicture();

}
private void setEditable(boolean editable){
	if(editable){
		this.findViewById(R.id.Front).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.BackActionBar).setVisibility(View.VISIBLE);
		editText.setEnabled(true);
		editTitle.setEnabled(true);
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
				new AlertDialog.Builder(EditActivity2.this).setTitle("图片操作")
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
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Bitmap btm=BitmapFactory.decodeStream(is);
							imgv.setImageBitmap(btm);
							EditActivity2.this.showPicture(true);
							
							
							try {
								is.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if(which==2){
							imgv.setImageBitmap(null);
							showPicture(false);
							
						}
					}
				}).show();
				
				return true;
			}
			
		});
	}else{
		this.findViewById(R.id.BackActionBar).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.Front).setVisibility(View.VISIBLE);
		
		editText.setEnabled(false);
		editTitle.setEnabled(false);
		mViews.get(1).setOnLongClickListener(null);
	
	}
}
@Override
protected void onResume(){
	super.onResume();

}
private void resetDetailsAndPicture(){
	editText.setText(content.content);			
	editTitle.setText(content.title);
	if(hasPicture){
		imgv.setImageBitmap(content.picture);
		showPicture(true);
	}else{
		imgv.setImageBitmap(null);
		showPicture(false);
	}
}
private void showPicture(boolean isShow){
	if(isShow)
	{
		imgv.setVisibility(View.VISIBLE);
		textView.setVisibility(View.INVISIBLE);
	}else{
		imgv.setVisibility(View.INVISIBLE);
		textView.setVisibility(View.VISIBLE);
	}
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
private class MyListener  implements OnClickListener{

	@Override
	public void onClick(View v) {
		if(v==btnBack){
			finish();
		}
		if(v==btnSave){
			String tmp=editTitle.getText().toString().trim();
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
			
		
			if(!imgv.isShown()){
				content.picture=null;
				hasPicture=false;
			}else{
				content.picture=((BitmapDrawable)imgv.getDrawable()).getBitmap();
				hasPicture=true;
			}
			content.title=editTitle.getText().toString();
			content.content=editText.getText().toString();
			HelloActivity.userDao.saveData(id, rowid, content);
			
			EditActivity2.this.resetDetailsAndPicture();
			finish();
		}
		if(v==btnEdit){
			EditActivity2.this.setEditable(true);
		}
		if(v==btnBack2){
			EditActivity2.this.resetDetailsAndPicture();
			EditActivity2.this.setEditable(false);
		}
	}
	
}
@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
	super.onActivityResult(requestCode, resultCode, data);
	if(resultCode==Activity.RESULT_OK){
		Bundle bundle=data.getExtras();
		Bitmap tmpPicture = (Bitmap) bundle.get("data");imgv.setImageBitmap(null);
		imgv.setImageBitmap(tmpPicture);
		showPicture(true);
	}
}
}
