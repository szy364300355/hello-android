package com.example.helloandroid.memeryCache;



public interface MemoryCacheI<Key, Resource> {
	
	/**
	 * 设定缓存大小
	 * @param limit
	 */
public void setLimit(long limit);
/**
 * 当前缓存大小
 * @param currentSize
 */
public void setCurrentSize(long currentSize);
/**
 * 根据key值获取缓存内容
 * @param key
 * @return
 */
public Resource getResource(Key key);
/**
 * 将内容保存到缓存中去
 * @param key
 * @param resource
 */
public void putResource(Key key,Resource resource);
/**
 * 检查当前占用大小是否超过限制
 */
public void checkSize();
/**
 * 清空缓存
 */
public void clear();
}
