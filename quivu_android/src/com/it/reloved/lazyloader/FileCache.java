package com.it.reloved.lazyloader;

import java.io.File;
import android.content.Context;
import android.util.Log;

public class FileCache {
    private String TAG="lazyloader <---> FileCache";
    private File cacheDir;
    
    public FileCache(Context context){
        //Find the dir to save cached images
    	try{
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"ImageCache");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }catch (Exception e1) {
		e1.printStackTrace();
		Log.e(TAG, "Exception FileCache(Context context)");
		
	}catch (Error e1) {
		e1.printStackTrace();
		Log.e(TAG, "Error in FileCache(Context context)");
	}
    }
    
    public File getFile(String url){
    	try{
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
    }catch (Exception e1) {
		e1.printStackTrace();
		Log.e(TAG, "Exception getFile(String url)");
		return null;
	}catch (Error e1) {
		e1.printStackTrace();
		Log.e(TAG, "Error in getFile(String url)");
		return null;
	}
    }
    
    public void clear(){
    	try{
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }catch (Exception e1) {
		e1.printStackTrace();
		Log.e(TAG, "Exception in clear()");
		
	}catch (Error e1) {
		e1.printStackTrace();
		Log.e(TAG, "Error in clear()");
	}
    }

}