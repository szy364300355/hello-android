package com.example.helloandroid.database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.helloandroid.EditActivity;
import com.example.helloandroid.EditActivity.Item;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class UserDao {
	userDatabaseHelper helper=null;
	private final Context context;
	private SQLiteDatabase db;
	
	public static final String USERDATA_BREIF="brief";
	public static final String USERDATA_CONTENT="content";
	public static final String USERDATA_DATE="date";
	public static final String USERDATA_TITLE="title";
	public UserDao(Context cxt){
		this.context=cxt;
		helper=new userDatabaseHelper(cxt);
	}
	/**
	 * 插入一个用户信息到user表中
	 * @param bundle 需包含account，pass 两个值
	 */
	public void insertUser(String account,String pass){
		try{
		String sql="insert into user (username,password) values(?,?)";
		openToWrite();
		db.execSQL(sql, new String[]{account,pass});		
		}catch(SQLException e){
			
		}finally{
			close();
		}
	}
	//打开数据库 --write
	public SQLiteDatabase openToWrite() throws SQLException{
		db=helper.getWritableDatabase();
		return db;
	}
	public SQLiteDatabase openToRead() throws SQLException{
		db=helper.getReadableDatabase();
		return db;
	}
	public void close(){
		helper.close();
	}
	public boolean ifUserExist(String account){
		try{
		openToRead();
		String sql="select * from user where username=?";
		Cursor c=db.rawQuery(sql, new String[]{account});
		if(c.moveToFirst())//查询结果不为空
			return true;
		Log.i("INFO","USER NOT EXIST");
		return false;
		}catch(SQLException e){
			return true;
		}finally{
			close();
		}
	}
	public boolean checkUserAndPass(String account,String pass){
		try{
			openToRead(); 
		String sql="select * from user where username=? and password=?";
		Cursor c=db.rawQuery(sql, new String[]{account,pass});
		if(!c.moveToFirst())//查询结果为空
			return false;
		
		return true;
		}catch(SQLException e){
			return false;
		}finally{
			close();
		}
	}
	public void insertData(String id,String brief,String content,String title){
		try{
			
			String cont="";
			if(content!=null)
				cont=content;
			Date dt=new Date(); 
			SimpleDateFormat matter=new SimpleDateFormat("yyyy/MM/dd");
			String date=matter.format(dt);
			String sql="insert into userdata (id,brief,content,title,date) values(?,?,?,?,?)";
			openToWrite();
			db.execSQL(sql, new Object[]{id,brief,cont,title,date});
			Log.i("INFO","insertData : 	"+id+" : "+brief);
		}catch(SQLException e){
			
		}finally{
			close();
		}
	}
	/**
	 *  在数据库中重新创建表 user userdata
	 */
	public void createAll(){
		try{
		openToWrite();
		helper.onUpgrade(db, 1, 1);
		}catch(SQLException e){
			
		}finally{
			close();
		}
	}
	/**
	 * 删除表名对应的的表中所有数据
	 * @param tablename
	 */
	public void delectAll(String tablename){
		try{
		String sql="delete from `"+tablename+"`";
		String sql2="";
		openToWrite();
		db.execSQL(sql);
		}catch(SQLException e){
			
		}finally{
			close();
		}
	}
	public Cursor getUserData(String id){
			openToRead();
			String sql="select *,rowid from userdata where id=? order by time desc";
			Cursor c=db.rawQuery(sql,new String[]{id});
//			Cursor c=db.rawQuery(sql, null);
			return c;
			
	}
	public void saveData(String id,String rowid,EditActivity.Item item){
		String sql="UPDATE userdata SET id =?, content = ?, brief =?,title=?,date=? WHERE rowid=?;";
		try{
			openToWrite();
			
			String brief;
			if(item.content.length()>40){
				brief=item.content.substring(0,39);			
			}else{
				brief=item.content;
			}
			db.execSQL(sql, new Object[]{id,item.content,brief,item.title,item.date,rowid});
		}catch(SQLException e){
		}finally{
			close();
		}
	}
	/**
	 * 根据rowid获取 用户备忘录内容
	 * @param rowid
	 * @return
	 */
	public EditActivity.Item getContent(String rowid){
		try{
			EditActivity.Item item=new EditActivity.Item();
			int po=Integer.valueOf(rowid);
			openToRead();
			String sql="select *,rowid from userdata where rowid=? order by time desc";
			Cursor c=db.rawQuery(sql,new String[]{rowid});
			if(c.moveToNext())
			{
				item.content=c.getString(c.getColumnIndex(UserDao.USERDATA_CONTENT));
				item.date=c.getString(c.getColumnIndex(UserDao.USERDATA_DATE));
				item.title=c.getString(c.getColumnIndex(UserDao.USERDATA_TITLE));
			}else{
				item.content="";
				item.date="";
				item.title="";
			}
			c.close();
			return item;
		}catch(SQLException e){
			return null;
		}finally{
			close();
		}
	}
	public int getRowId(String username){
		try{
			openToRead();
			String sql="select *,rowid from user where username=?";
			Cursor c=db.rawQuery(sql, new String[]{username});
			if(c.moveToFirst()){
				return c.getInt(c.getColumnIndex("rowid"));
			}
			return -1;
		}catch(SQLException e){
			return -1;
		}finally{
			close();
		}
		
	}
	/**
	 * 删除listview对应position位置的数据
	 * @param position
	 */
	public void delectItems(List position,Cursor c){
		c.close();
		StringBuffer sql=new StringBuffer(); StringBuffer arg=new StringBuffer(); 
		sql.append("DELETE FROM userdata WHERE rowid IN ");
		arg.append("(");
		for(int i=0;i<position.size()-1;i++)
			{
			arg.append(position.get(i));
			arg.append(",");
			}
		arg.append(position.get(position.size()-1));
		arg.append(");");
		sql.append(arg.toString());
		Log.i("INFO","delectData : 	"+sql.toString());
		try{
			openToWrite();
			db.execSQL(sql.toString());
		}catch(SQLException e){
		}finally{
			close();
		}
	}
	}
