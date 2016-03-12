package com.example.helloandroid.memeryCache;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.helloandroid.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;


public class AsyncImageLoader implements AsyncLoaderI{
	//定义缓存
	public	ImageMemoryCacheImpl imageMemoryCache; 
	//定义线程池
	private ExecutorService executorService;
	private Context context;
	private Drawable defaultImageMini;
	private Drawable defaultImageBig;
	//handler 发送消息用
	private final static Handler handler=new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case LOAD_IMAGE:
				ImageModel imageModel = (ImageModel) message.obj;
				//调用回调函数更改UI
				imageModel.imageCallBack.changUI(imageModel.drawable,
						imageModel.path);
				break;
			}
		}
	};
	/**
	 * 由adapter调用此函数做图片展示，首先从缓存中获取，没有则开启线程读取，读取后调用回调函数更新
	 * @param path
	 * @param kind
	 * @param asyncLoaderCallBackI
	 * @return
	 */
	public Drawable displayImage(String path, int kind,
			AsyncLoaderCallBackI<Drawable, String> asyncLoaderCallBackI) {
		Bitmap bitmap = imageMemoryCache.getResource(path);
		if (bitmap != null) {//缓存中取到数据
			Drawable drawable = new BitmapDrawable(context.getResources(),
					bitmap);
			return drawable;
		}
		//缓存中没有，开启异步线程读取数据
		threadLoadImage(path, kind, asyncLoaderCallBackI);
		return null;
	}
	
	private void threadLoadImage(String path, int kind,
			AsyncLoaderCallBackI<Drawable, String> asyncLoaderCallBackI) {
		ImageModel imageModel = new ImageModel(path, kind, asyncLoaderCallBackI);
		ImageThreadLoader imageLoader = new ImageThreadLoader(imageModel);
		executorService.execute(imageLoader);
	}
	class ImageThreadLoader implements Runnable{
		Bitmap bitmap;
		ImageModel imageModel;
		public ImageThreadLoader(ImageModel imageModel){
			this.imageModel=imageModel;
		}
		@Override
		public void run() {//执行线程读取数据
			Bitmap bitmap = null;
			bitmap = Utils.getImage(imageModel.path, imageModel.kind);
			// 添加内存缓存
			imageMemoryCache.putResource(imageModel.path, bitmap);
			Drawable drawable = new BitmapDrawable(context.getResources(),
					bitmap);
			imageModel.drawable = drawable;
			Message message = handler.obtainMessage(LOAD_IMAGE,
					imageModel);
			handler.sendMessage(message);	
		}
		
		
	}

	public AsyncImageLoader(Context context){
		this.context = context;
		executorService = Executors.newFixedThreadPool(4);
		imageMemoryCache = new ImageMemoryCacheImpl();
	}
	
	public class ImageModel {
		public AsyncLoaderCallBackI imageCallBack;
		public Drawable drawable=null;
		public String path;
		public int kind;

		public ImageModel(Drawable drawable, String path, int kind,
				AsyncLoaderCallBackI imageCallBack) {
			this.drawable = drawable;
			this.path = path;
			this.imageCallBack = imageCallBack;
			this.kind = kind;
		}
		public ImageModel( String path, int kind,
				AsyncLoaderCallBackI<Drawable, String> asyncLoaderCallBackI) {
			this.path = path;
			this.imageCallBack = asyncLoaderCallBackI;
			this.kind = kind;
		}
		public ImageModel setDrawable(Drawable drawable ){
			this.drawable=drawable;
			return this;
		}
	}  
	
	//在Adapter中定义
//	class ImageCallBack implements AsyncLoaderCallBackI<Drawable,String>{
//
//		@Override
//		public void changUI(Drawable resouce, String tag) {
//			
//		}
//		
//	}
	
}
