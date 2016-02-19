package com.example.helloandroid;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment  extends Fragment{
	private FrameActivity parent;
	private View frame=null;
	private ImageView imageView=null;
	public Bitmap btmap;
	
	public void ImageFragment(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		Log.i("Fragment2", "-------------------create!!!!!-------------------");
		super.onCreate(savedInstanceState);
//		parent=(FrameActivity) getActivity();
//		imageView=(ImageView)frame.findViewById(R.id.frame_layout2_imageView);
//		imageView.setClickable(true);
//		imageView.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				parent.showFrag1(true);
//				imageView.setImageBitmap(null);
//			}
//			
//		});
	}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		Log.i("Fragment2", "-------------------create view!!!!!-------------------");
		super.onCreateView(inflater, container, savedInstanceState);
		if(container==null)
			return null;
		this.frame=inflater.inflate(R.layout.frame_layout2, container, false);
		
		parent=(FrameActivity) getActivity();
		imageView=(ImageView)frame.findViewById(R.id.frame_layout2_imageView);
		imageView.setClickable(true);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				parent.fragment1.clickItem=-1;
				parent.showFrag1(true);
				imageView.setImageBitmap(null);
			}
			
		});
		
		
		return frame;
	}
	public void showPicture(){
		this.imageView.setImageBitmap(btmap);
	}
	@Override
	public void onResume(){
		super.onResume();
		imageView.setImageBitmap(btmap);
	}
}
