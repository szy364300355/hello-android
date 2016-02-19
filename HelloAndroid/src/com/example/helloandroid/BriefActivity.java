package com.example.helloandroid;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.helloandroid.database.UserDao;
import com.example.helloandroid.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BriefActivity extends Activity{
	private long rowid;
	private Button btnBack;
	private Button btnRepeal;
	private Button btnEdit;
	private Button btnDelete;
	boolean visflag = false;
	List<Integer> selected=new ArrayList<Integer>();
	private  String userid;
	 /*定义一个动态数组*/     
	private ArrayList<HashMap<String, Object>>listItem;
	
	
	private ListView listview;
	private Context context;
	private List<String> position;//=new ArrayList<String>();
	private boolean isMulChoice=false;//多选
	public Adapter adapter;
	private LinearLayout layout;
	private TextView txtcount;
	public Cursor c;
	private Bundle savedInstanceState;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState=savedInstanceState;
		userid=getIntent().getExtras().getString("id");
		setContentView(R.layout.brief_layout); 
		
		context=this;
		
		listview=(ListView)findViewById(android.R.id.list);
		listview.setLongClickable(true);
		
		
		layout=(LinearLayout)findViewById(R.id.briefHideLayout);
		
		btnBack=(Button)this.findViewById(R.id.btnBriefBack);
		btnBack.setOnClickListener(new MyListener());
		btnRepeal=(Button)this.findViewById(R.id.btnBriefRepeal);
		btnRepeal.setOnClickListener(new MyListener());
		
		btnEdit=(Button)this.findViewById(R.id.btnBriefAdd);
		btnEdit.setOnClickListener(new MyListener());
		btnDelete=(Button)this.findViewById(R.id.btnBriefDelete);
		btnDelete.setOnClickListener(new MyListener());
		
		
    }
	
	@Override
    protected void onResume(){
    	super.onResume();
    	c=HelloActivity.userDao.getUserData(userid);
		adapter=new Adapter(this,c);
//    	c=HelloActivity.userDao.getUserData(userid);
//		Adapter adapter=new Adapter(this,c);
//
//		listview.setAdapter(adapter);
    		
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Log.v("MyListViewBase", "你点击了ListView条目" + arg2);//在LogCat中输出信息
				 
				 //跳转查看/编辑页面
				 Bundle bundle=new Bundle(); 
				 Adapter adapter=BriefActivity.this.adapter;
				 String rowid=adapter.getRowid(arg2);
				 bundle.putString("rowid",rowid);				 
				 bundle.putString("id",getIntent().getExtras().getString("id"));
				 bundle.putBoolean("hasPicture", adapter.hasPicture(arg2));
				 Intent intent = new Intent(); 
				 intent.putExtras(bundle);
				 intent.setClass(BriefActivity.this, EditActivity2.class);
				 
				BriefActivity.this.startActivity(intent);
				
			}
        });
		listview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				visflag=true;
				((BaseAdapter) listview.getAdapter()).notifyDataSetInvalidated();
				findViewById(R.id.briefFront).setVisibility(View.GONE);
				findViewById(R.id.briefHideLayout).setVisibility(View.VISIBLE);
				return false;
			}
			
		});
    }
	@Override
    protected void onStop(){
		super.onStop();
		if(c!=null&&!c.isClosed())
			c.close();
			HelloActivity.userDao.close();
	}
	
	private class MyListener implements OnClickListener{
	
		@Override
		public void onClick(View v) {
			if(v==btnBack){
				finish();
			}
			if(v==btnEdit){
				 Bundle bundle=new Bundle(); 
				 bundle.putString("id", getIntent().getExtras().getString("id"));
				 Intent intent = new Intent(); 
			     intent.setClass(BriefActivity.this, AddActivity2.class);
			     intent.putExtras(bundle);
			     BriefActivity.this.startActivity(intent);
			}
			if(v==btnRepeal){
				findViewById(R.id.briefFront).setVisibility(View.VISIBLE);
				findViewById(R.id.briefHideLayout).setVisibility(View.GONE);
					visflag=false;
					((BaseAdapter) listview.getAdapter()).notifyDataSetInvalidated();
					return;
			}
			if(v==btnDelete){
				final Adapter adapter=(Adapter) listview.getAdapter();
				if(adapter.selectCount==0)
					return;
				new AlertDialog.Builder(BriefActivity.this).setTitle("提示").setMessage("确认删除当前选择的"+adapter.selectCount+"项么？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						position=new ArrayList<String>();
						HashMap<Integer,Boolean> checkBox=adapter.ischeck;
						for(int i=0;i<checkBox.size();i++)
							if(checkBox.get(i))
								{
								position.add(String.valueOf(adapter.visiblecheck.get(i)));
								}
						HelloActivity.userDao.delectItems(position,BriefActivity.this.c);
						visflag=false;
						((BaseAdapter) listview.getAdapter()).notifyDataSetInvalidated();
						onResume();
						findViewById(R.id.briefFront).setVisibility(View.VISIBLE);
						findViewById(R.id.briefHideLayout).setVisibility(View.GONE);
						
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

			}
		}
	}

	public class Adapter extends BaseAdapter{
		private Context context;
		private LayoutInflater inflater=null;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/ 
		private  int convertViewCounter=10;
		private HashMap<Integer,View> mView;
		public  HashMap<Integer, Integer> visiblecheck ;
		public HashMap<Integer,Boolean> ischeck;
		private Cursor cursor=null;
		public int selectCount=0;
		public Adapter(Context context,Cursor c){
			
			this.context=context;
			this.cursor=c;
			this.convertViewCounter=c.getCount();
			inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			mView=new HashMap<Integer,View>();
			visiblecheck= new HashMap<Integer, Integer>();
			ischeck= new HashMap<Integer, Boolean>();
			 init();
			
		}
		public boolean hasPicture(int position) {
			// TODO Auto-generated method stub
			c.moveToPosition(position);
			byte[] re=c.getBlob(cursor.getColumnIndex(UserDao.USERDATA_PICTURE));
			if(re==null)
			return false;
			return true;
		}
		public void init(){
			
			for(int i=0;i<getCount();i++){
				ischeck.put(i, false);
				visiblecheck.put(i, -1);
			}
		}
		public String getRowid(int position){
			c.moveToPosition(position);
			return cursor.getString(cursor.getColumnIndex("rowid"));
		}
		@Override
		public int getCount() {
			return convertViewCounter;
		}

		@Override
		public Object getItem(int position) {
			c.moveToPosition(position);
			String content=cursor.getString(cursor.getColumnIndex(UserDao.USERDATA_CONTENT));
			return content;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final int p = position;
			//观察convertView随ListView滚动情况             
			Log.v("MyListViewBase", "getView " + position + " " + convertView);
			
			if (convertView == null) {
				 convertView = inflater.inflate(R.layout.item,
						    null);
				 holder = new ViewHolder();
                 /*得到各个控件的对象*/ 
				 holder.imgv=(ImageView)convertView.findViewById(R.id.itemImage);
				 holder.title= (TextView) convertView.findViewById(R.id.itemTitle);
				 holder.brief = (TextView) convertView.findViewById(R.id.itemBrief);
                 holder.date = (TextView) convertView.findViewById(R.id.itemDate);
                 holder.bt = (CheckBox) convertView.findViewById(R.id.itemCheck);
                 holder.bt.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox)v;
						if(cb.isChecked())
							{
							selectCount++;
							}else{
							selectCount--;
							}
						ischeck.put(p,cb.isChecked());
						BriefActivity.this.c.moveToPosition(p);
						int rowid=cursor.getInt(cursor.getColumnIndex("rowid"));
						visiblecheck.put(p, rowid);
					}
                	 
                 });
                 
                 convertView.setTag(holder);//绑定ViewHolder对象    

			} else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象

                }
			
			if(visflag){
				holder.bt.setVisibility(View.VISIBLE);
			}else{
				holder.bt.setChecked(false);
				init();
				holder.bt.setVisibility(View.GONE);
				
			}
            if(cursor.moveToPosition(position)){
				String rowid=cursor.getString(cursor.getColumnIndex("rowid"));
				Bitmap tmp=HelloActivity.userDao.getPicture(rowid);
				holder.title.setText(cursor.getString(cursor.getColumnIndex(UserDao.USERDATA_TITLE)));
				holder.brief.setText(cursor.getString(cursor.getColumnIndex(UserDao.USERDATA_BREIF)));
				holder.date.setText(cursor.getString(cursor.getColumnIndex(UserDao.USERDATA_DATE)));
				if(tmp==null){
					holder.imgv.setImageBitmap(null);
					holder.imgv.setVisibility(View.GONE);
				}else{
					holder.imgv.setImageBitmap(Utils.getSquareBitmap(tmp));
					holder.imgv.setVisibility(View.VISIBLE);
				}
			}
			 return convertView;
		}
		/**存放控件*/ 
		public final class ViewHolder{
			public ImageView imgv;
			public TextView title;
		    public TextView brief;
		    public TextView date;
		    public CheckBox   bt;
		    }

	}
}
