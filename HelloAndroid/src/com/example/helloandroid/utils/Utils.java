package com.example.helloandroid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.util.DisplayMetrics;

public class Utils {
	public static final int ORIGINAL_SIZE=1;
	public static final int MINIMUM_SIZE_SQUARE=0;
	public static  int MAX_WIDTH;
	public static  int MAX_HEIGHT;
	public static final int MINI_SIZE=100;
	
	/**
	 * 根据设定需要获取图片种类 <p>ORIGINAL_SIZE<p> MINIMUM_SIZE_SQUARE
	 * @param path
	 * @param kind
	 * @return
	 */
	public static Bitmap getImage(String path,int kind){
		switch (kind){
		case ORIGINAL_SIZE:
			return getImage(path,MAX_WIDTH,MAX_HEIGHT);
		case MINIMUM_SIZE_SQUARE:
			return getSquareBitmap(getImage(path,MINI_SIZE,MINI_SIZE));
		}
		return null;
	}
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
			Bitmap newpicture;
			newpicture=Bitmap.createBitmap(picture, 0, 0, pictureWidth, pictureHeight, matrix, true);
			picture.recycle();
		    return newpicture;
	}
	/**
	 * 截取图片正方形
	 */
	public static Bitmap getSquareBitmap(Bitmap bitmap){
		int widthOrg=bitmap.getWidth();
		int heightOrg=bitmap.getHeight();
		
		int square=Math.min(widthOrg, heightOrg);
		int xTopLeft=(widthOrg-square)/2;
		int yTopLeft=(heightOrg-square)/2;
		Bitmap result=Bitmap.createBitmap(bitmap,xTopLeft ,yTopLeft ,square ,square );
		bitmap.recycle();
		return result;
	}
	
	/**
	 * 根据路径从相册中获取图片
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getImage(String path,int width,int height){
		
		BitmapFactory.Options options=new 	BitmapFactory.Options();
		   options.inJustDecodeBounds=true;
		   Bitmap imb=BitmapFactory.decodeFile(path,options);
			
			options.inJustDecodeBounds=false;
			
			int h=options.outHeight;
			int w=options.outWidth;
			
			int beWidth=w/width;
			int beHeight=h/height;
			
			int be=1;
			
			if(beWidth<beHeight){
				be=beWidth;
			}else{
				be=beHeight;
			}
		   if(be<=0){
			   be=1;
		   }
		   options.inSampleSize=be;
		   
		   imb=BitmapFactory.decodeFile(path,options);
//		   imb=ThumbnailUtils.extractThumbnail(imb, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
				   
			return imb;
	}
}
