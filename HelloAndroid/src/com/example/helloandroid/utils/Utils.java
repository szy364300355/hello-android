package com.example.helloandroid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class Utils {
	/**
	 * 调整图片适应屏幕大小显示
	 * @param picture
	 */
	public static Bitmap initPicture(Bitmap picture,Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		int height=dm.heightPixels-80;
		int pictureWidth=picture.getWidth();
		int pictureHeight=picture.getHeight();
		if(pictureWidth==0||pictureHeight==0)
			return picture;
	//计算缩放比例
		float scaleWidth=(float)width/pictureWidth;
		float scaleHeight=(float)height/pictureHeight;
		
		float scale=Math.min(scaleWidth, scaleHeight);
		Matrix matrix=new Matrix();
		matrix.postScale(scale, scale);
		
	    picture=Bitmap.createBitmap(picture, 0, 0, pictureWidth, pictureHeight, matrix, true);
	    return picture;
		
	}
	/**
	 * 调整图片大小
	 */
	public static Bitmap resizePicture(Bitmap picture,int newWidth,int newHeight){
		int pictureWidth=picture.getWidth();
		int pictureHeight=picture.getHeight();
		if(pictureWidth==0||pictureHeight==0)
			return picture;
		//计算缩放比例
			float scaleWidth=(float)newWidth/pictureWidth;
			float scaleHeight=(float)newHeight/pictureHeight;
			
			float scale=Math.min(scaleWidth, scaleHeight);
			Matrix matrix=new Matrix();
			matrix.postScale(scale, scale);
			
		    picture=Bitmap.createBitmap(picture, 0, 0, pictureWidth, pictureHeight, matrix, true);
		    return picture;
	}
}
