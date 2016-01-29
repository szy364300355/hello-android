package com.example.helloandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class userDatabaseHelper extends SQLiteOpenHelper{
	//定义数据库名
	private final static String DB_NAME="userdata.db";
	//版本号
	private final static int VERSION=1;
	
	public userDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	public userDatabaseHelper(Context cxt){
		this(cxt,DB_NAME,null,VERSION);
	}
	//版本变更时
	public userDatabaseHelper(Context cxt,int version){
		this(cxt,DB_NAME,null,version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//数据库创建时调用
		String sql="CREATE TABLE user ("+
				"`username` varchar( 20 ) NOT NULL ,`password` varchar( 20 ) NOT NULL);";
		String sql2="CREATE TABLE userdata(`id` int( 11 ) NOT NULL ," +
				"`brief` text NOT NULL ,`content` text DEFAULT '',`title` text NOT NULL ,`picture` blob DEFAULT NULL," +
				"`time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ," +
				"`date` varchar( 20 ) NOT NULL) ";
		
		db.execSQL(sql);
		db.execSQL(sql2);
		db.execSQL("create index userdata_index on userdata(id);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql="drop table if exists user";
		String sql2="drop table if exists userdata";
		db.execSQL(sql);
		db.execSQL(sql2);
		onCreate(db);
	}

}
