package com.example.helloandroid;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.helloandroid.BriefActivity.Adapter.ViewHolder;
import com.example.helloandroid.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;


public class SumFragment extends Fragment{
private Button btn;
private View frame=null;
private GridView gridview;
public CheckBox selectedItem=null;
public int selectPosition=-1;
public ImageAdapter adapter;
private FrameActivity parent;
public int clickItem=-1;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		
		
		Log.i("Fragment1", "-------------------create view!!!!!-------------------");
		super.onCreateView(inflater, container, savedInstanceState);
		if(container==null)
			return null;
		this.frame=inflater.inflate(R.layout.frame_layout, container, false);		
		gridview=(GridView)frame.findViewById(R.id.frameGridView);
		adapter=new ImageAdapter(this.getActivity());
		parent=(FrameActivity)this.getActivity();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				parent.showFrag1(false);
			}
			
		});
		return frame;
	}
	@Override
	public void onDestroy(){
		Log.i("Fragment1", "-------------------Destroy!!!!!-------------------");
		super.onDestroy();
		adapter.cursor.close();
	}
	public void close(){
		if(!adapter.cursor.isClosed()){
			adapter.cursor.close();
		}
	}
	public Bitmap getSelectImage(){
		WindowManager wm=parent.getWindowManager();
		
		return adapter.getImage(clickItem,wm.getDefaultDisplay().getWidth(),wm.getDefaultDisplay().getHeight());
	}
	//构建适配器
	public class ImageAdapter extends BaseAdapter{
		private Context context;
		private final String IMAGE_TYPE="image/*";
		private Cursor cursor;
		private LayoutInflater inflater=null;
		private String[] filePathColumns={MediaStore.Images.Media.DATA};
		private int column_index;
		private Map<Integer,CheckBox> checkBoxList;

		public ImageAdapter(Context c){
			this.context=c;
			checkBoxList=new HashMap<Integer,CheckBox>();
			inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			cursor=SumFragment.this.getActivity().getContentResolver().
			query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumns, null, null, null);
			column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		}

		public String getImageString(int position){
			if(cursor.isClosed()){
				cursor=parent.getContentResolver().
				query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumns, null, null, null);
			}
			cursor.moveToPosition(position);
			   String selectedImage=cursor.getString(column_index);
			   return selectedImage;
		}
		public Bitmap getImage(int position,int width,int height){
			cursor.moveToPosition(position);
		   String selectedImage=cursor.getString(column_index);
		   return Utils.getImage(selectedImage, width, height);
		}
//		
		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;  
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				 convertView = inflater.inflate(R.layout.imageitem,
						    null);
				 holder = new ViewHolder();
				 holder.imgv=convertView.getBackground();
				 holder.bt=(CheckBox) convertView.findViewById(R.id.itemCheck);
				 checkBoxList.put(position,  holder.bt);
				 convertView.setTag(holder);//绑定ViewHolder对象    
				 
			}else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
                }
			holder.imgv=new BitmapDrawable(Utils.getSquareBitmap(getImage(position,100,100)));
			convertView.setBackground(holder.imgv);
			holder.bt= (CheckBox) convertView.findViewById(R.id.itemCheck);
			
			if(position!=selectPosition&&holder.bt.isChecked()){
				holder.bt.setChecked(false);
			}
			if(position==selectPosition&&!holder.bt.isChecked()){
				holder.bt.setChecked(true);
			}
			
			holder.bt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(selectedItem==v){
						selectedItem=null;
						selectPosition=-1;
						
					}
					if(selectedItem!=null&&selectedItem!=v){
						selectedItem.setChecked(false);
						selectedItem=(CheckBox) v;
						selectPosition=position;
					}
					if(selectedItem==null){
						selectedItem=(CheckBox) v;
						selectPosition=position;
					}
				}
				
			});
			convertView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
						SumFragment.this.clickItem=position;
					SumFragment.this.parent.showFrag1(false);
				}
				
			});
			return convertView;
		}
		
		/**存放控件*/ 
		public final class ViewHolder{
			public Drawable imgv;
		    public CheckBox   bt;
		}
	}
}
