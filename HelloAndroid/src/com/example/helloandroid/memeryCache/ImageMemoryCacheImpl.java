package com.example.helloandroid.memeryCache;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;

public class ImageMemoryCacheImpl implements MemoryCacheI<String, Bitmap> {
	private long limit=0l;
	private long currentSize=0l;
	public final static long DEFAULT_LIMIT=Runtime.getRuntime().maxMemory() / 4;

	//需要输出的顺序和输入的相同--LinkedHashMap
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	
	public ImageMemoryCacheImpl(){
		setLimit(DEFAULT_LIMIT);
	}
	public ImageMemoryCacheImpl(long limit){
		setLimit(limit);
	}
	
	@Override
	public void setLimit(long limit) {
		//最大内存的四分之一
		this.limit =limit;
	}

	@Override
	public void setCurrentSize(long currentSize) {
		this.currentSize=currentSize;
	}

	/**
	 * 根据path获取图片
	 */
	@Override
	public Bitmap getResource(String path) {
		try {
			if (!cache.containsKey(path)) {
				return null;
			}
			return cache.get(path);
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * 将path做key 将图片放入缓存
	 */
	@Override
	public void putResource(String path, Bitmap image) {
		if (!cache.containsKey(path)) {
			cache.put(path, image);
			currentSize = currentSize + getImageSize(image);
			checkSize();
		}
		
	}

	private long getImageSize(Bitmap image) {
		if (image == null) {
			return 0;
		}
		return image.getRowBytes() * image.getHeight();
	}

	@Override
	public void checkSize() {

		if (currentSize > limit) {
			//超出内存限制
			Iterator<Entry<String, Bitmap>> iterator = cache.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				try {
					Entry<String, Bitmap> entry = iterator.next();
					currentSize = currentSize - getImageSize(entry.getValue());
					//删除头部元素
					iterator.remove();
				} catch (ConcurrentModificationException e) {
					e.printStackTrace();
				}
				if (currentSize < limit) {//直到没有超出内存限制
					break;
				}
			}
		}
			
	}

	@Override
	public void clear() {
		cache.clear();
		
	}

}
