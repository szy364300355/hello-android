package com.example.helloandroid.memeryCache;
/**
 * 
 * 
 *		异步加载器修改UI具体操作的回调函数
 * @param <Resouce>
 * @param <Tag>
 */
public interface AsyncLoaderCallBackI <Resouce,Tag> {
	/**
	 * 通过tag找到需要改变的View，然后将resouce设置进去
	 * @param resouce
	 * @param tag
	 */
	public void changUI(Resouce resouce, Tag tag);
}
