package com.it.reloved.lazyloader;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class Utils {
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=8192;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
        catch (Error e1) {
    		e1.printStackTrace();
    		Log.e("lazyloader <---> Utils", "Error in CopyStream");
    		
    	}
    }
}