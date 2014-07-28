package com.it.reloved.lazyloader;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {
    private HashMap<String, SoftReference<Bitmap>> cache=new HashMap<String, SoftReference<Bitmap>>();
    private String TAG="lazyloader <---> MemoryCache";
    
    public Bitmap get(String id){
    	try{
        if(!cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();
    }catch (Exception e1) {
		e1.printStackTrace();
		Log.e(TAG, "Exception in get(String id)");
		 return null;
	}catch (Error e1) {
		e1.printStackTrace();
		Log.e(TAG, "Error in get(String id)");
		 return null;
	}
    }
    
    public void put(String id, Bitmap bitmap){
    	try{
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }catch (Exception e1) {
		e1.printStackTrace();
		Log.e(TAG, "Exception in put(String id, Bitmap bitmap)");
		
	}catch (Error e1) {
		e1.printStackTrace();
		Log.e(TAG, "Error in put(String id, Bitmap bitmap)");
	}
    }

    public void clear() {
    	try{
        cache.clear();
    }catch (Exception e1) {
		e1.printStackTrace();
		Log.e(TAG, "Exception in clear()");
		
	}catch (Error e1) {
		e1.printStackTrace();
		Log.e(TAG, "Error in clear()");
	}
    }
}